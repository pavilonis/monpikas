package lt.pavilonis.cmm.security.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.config.SecurityUserResultSetExtractor;
import lt.pavilonis.cmm.security.Role;
import lt.pavilonis.cmm.security.ui.SecurityUserFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SecurityUserRepository implements EntityRepository<SecurityUser, Long, SecurityUserFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   @Transactional
   public SecurityUser saveOrUpdate(SecurityUser user) {
      if (StringUtils.isBlank(user.getUsername())) {
         throw new IllegalArgumentException("no username");
      }
      return user.getId() == null
            ? save(user)
            : update(user);
   }

   private SecurityUser update(SecurityUser user) {
      var sql = "UPDATE User " +
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

   private SecurityUser save(SecurityUser user) {
      Map<String, Object> args = new HashMap<>();
      args.put("username", user.getUsername());
      args.put("name", user.getName());
      args.put("email", user.getEmail());
      args.put("enabled", user.isEnabled());
      args.put("password", passwordEncoder.encode(user.getPassword()));

      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update("" +
                  "INSERT INTO User (username, name, email, enabled, password)" +
                  " VALUES (:username, :name, :email, :enabled, :password)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      long userId = keyHolder.getKey().longValue();
      saveRoles(userId, user.getAuthorities());

      return find(userId).orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<SecurityUser> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<SecurityUser> load(SecurityUserFilter filter) {
      HashMap<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("username", StringUtils.stripToNull(filter.getUsername()));
      args.put("text", StringUtils.isBlank(filter.getText()) ? null : "%" + filter.getText() + "%");
      return jdbc.query("" +
                  "SELECT u.*, r.* " +
                  "FROM User u " +
                  "  LEFT JOIN UserRole ur ON ur.user_id = u.id " +
                  "  LEFT JOIN Role r ON r.id = ur.role_id " +
                  "WHERE " +
                  "  (:id IS NULL OR :id = u.id)" +
                  "  AND (:username IS NULL OR :username = u.username)" +
                  "  AND (:text IS NULL OR u.name LIKE :text OR u.username LIKE :text) " +
                  "ORDER BY u.name",
            args,
            new SecurityUserResultSetExtractor()
      );
   }

   @Override
   public Optional<SecurityUser> find(Long id) {
      List<SecurityUser> result = load(new SecurityUserFilter(id, null, null));
      if (result.size() > 1) {
         throw new IllegalStateException();
      }

      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM User WHERE id = :id", Map.of("id", id));
   }

   @Override
   public Class<SecurityUser> entityClass() {
      return SecurityUser.class;
   }
}
