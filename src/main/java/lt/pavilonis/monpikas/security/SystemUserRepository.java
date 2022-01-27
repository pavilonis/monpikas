package lt.pavilonis.monpikas.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.security.ui.SystemUserFilter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static lt.pavilonis.monpikas.common.util.TimeUtils.duration;

@Slf4j
@AllArgsConstructor
@Repository
public class SystemUserRepository implements EntityRepository<SystemUser, Long, SystemUserFilter>, UserDetailsService {

   private final NamedParameterJdbcTemplate jdbc;
   private final PasswordEncoder passwordEncoder;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      List<SystemUser> result = load(new SystemUserFilter(null, username, null));

      if (result.size() == 1) {
         return result.get(0);
      }

      if (result.isEmpty()) {
         throw new UsernameNotFoundException("User not found");
      } else {
         throw new IllegalStateException("Duplicate usernames found");
      }
   }

   @Override
   @Transactional
   public SystemUser saveOrUpdate(SystemUser user) {
      if (!StringUtils.hasText(user.getUsername())) {
         throw new IllegalArgumentException("no username");
      }
      return user.getId() == null
            ? save(user)
            : update(user);
   }

   private SystemUser update(SystemUser user) {
      var sql = "UPDATE SystemUser " +
            "SET name = :name, email = :email, enabled = :enabled, username = :username " +
            "WHERE id = :id";

      Map<String, Object> params = new HashMap<>();
      params.put("name", user.getName());
      params.put("email", user.getEmail());
      params.put("enabled", user.isEnabled());
      params.put("username", user.getUsername());
      params.put("id", user.getId());

      jdbc.update(sql, params);
      jdbc.update("DELETE FROM UserRole WHERE user_id = :id", Map.of("id", user.getId()));
      saveRoles(user.getId(), user.getAuthorities());

      return find(user.getId())
            .orElseThrow(IllegalStateException::new);
   }

   private void saveRoles(long userId, Collection<Role> roles) {
      var sql = "INSERT INTO UserRole (user_id, role_id) VALUES (:userId, :roleId)";
      roles.stream()
            .map(Role::getId)
            .distinct()
            .forEach(roleId -> jdbc.update(sql, Map.of("userId", userId, "roleId", roleId)));
   }

   private SystemUser save(SystemUser user) {
      Map<String, Object> args = new HashMap<>();
      args.put("username", user.getUsername());
      args.put("name", user.getName());
      args.put("email", user.getEmail());
      args.put("enabled", user.isEnabled());
      args.put("password", passwordEncoder.encode(user.getPassword()));

      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO SystemUser (username, name, email, enabled, password)" +
            " VALUES (:username, :name, :email, :enabled, :password)";
      jdbc.update(sql, new MapSqlParameterSource(args), keyHolder);

      long userId = keyHolder.getKey().longValue();
      saveRoles(userId, user.getAuthorities());

      return find(userId).orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<SystemUser> load() {
      throw new IllegalStateException("Not implemented - not needed yet");
   }

   @Override
   public List<SystemUser> load(SystemUserFilter filter) {
      var start = now();
      var args = new HashMap<String, Object>();
      args.put("id", filter.getId());
      args.put("username", StringUtils.hasText(filter.getUsername()) ? filter.getUsername().strip() : null);
      args.put("text", StringUtils.hasText(filter.getText()) ? "%" + filter.getText() + "%" : null);
      var sql = "SELECT u.*, r.* " +
            "FROM SystemUser u " +
            "  LEFT JOIN UserRole ur ON ur.user_id = u.id " +
            "  LEFT JOIN Role r ON r.id = ur.role_id " +
            "WHERE " +
            "  (:id IS NULL OR :id = u.id)" +
            "  AND (:username IS NULL OR :username = u.username)" +
            "  AND (:text IS NULL OR u.name LIKE :text OR u.username LIKE :text) " +
            "ORDER BY u.name";
      List<SystemUser> result = jdbc.query(sql, args, new SystemUserResultSetExtractor());
      log.debug("Loaded system users [size={}, t={}]", result.size(), duration(start));
      return result;
   }

   @Override
   public Optional<SystemUser> find(Long id) {
      List<SystemUser> result = load(new SystemUserFilter(id, null, null));
      if (result.size() > 1) {
         throw new IllegalStateException();
      }

      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM SystemUser WHERE id = :id", Map.of("id", id));
   }

   @Override
   public Class<SystemUser> entityClass() {
      return SystemUser.class;
   }

   public void changePassword(long userId, String newPassword) {
      var sql = "UPDATE SystemUser SET password = :pass WHERE id = :id";
      jdbc.update(sql, Map.of("id", userId, "pass", passwordEncoder.encode(newPassword)));
   }
}
