package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.security.ui.SecurityUserFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SecurityUserDetailsService implements UserDetailsService, EntityRepository<SecurityUser, Long, SecurityUserFilter> {

   private static final SecurityUserResultSetExtractor EXTRACTOR = new SecurityUserResultSetExtractor();

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   public UserDetails loadUserByUsername(String username) {
      List<SecurityUser> result = load(new SecurityUserFilter(null, username, null));

      if (result.size() > 1) {
         throw new IllegalStateException("Duplicate usernames?");

      } else if (result.size() == 1) {
         return result.get(0);
      }
      return null;
   }

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
      jdbc.update(
            "UPDATE User SET name = ?, email = ?, enabled = ?, username = ? WHERE id = ?",
            user.getName(), user.getEmail(), user.isEnabled(), user.getUsername(), user.getId()
      );

      jdbc.update("DELETE FROM UserRole WHERE user_id = ?", user.getId());
      user.getAuthorities()
            .forEach(role -> jdbc.update(
                  "INSERT INTO UserRole (user_id, role_id) VALUES (?, ?)",
                  user.getId(), role.getId()
            ));

      return find(user.getId())
            .orElseThrow(IllegalStateException::new);
   }

   private SecurityUser save(SecurityUser user) {
      String temporaryPassword = passwordEncoder.encode(user.getUsername());
      Map<String, Object> args = new HashMap<>();
      args.put("username", user.getUsername());
      args.put("name", user.getName());
      args.put("email", user.getEmail());
      args.put("enabled", user.isEnabled());
      args.put("password", temporaryPassword);

      KeyHolder keyHolder = new GeneratedKeyHolder();
      namedJdbc.update("" +
                  "INSERT INTO User (username, name, email, enabled, password)" +
                  " VALUES (:username, :name, :email, :enabled, :password)",
            new MapSqlParameterSource(args),
            keyHolder

      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<SecurityUser> load(SecurityUserFilter filter) {
      HashMap<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("username", StringUtils.stripToNull(filter.getUsername()));
      args.put("text", StringUtils.isBlank(filter.getText()) ? null : "%" + filter.getText() + "%");
      return namedJdbc.query("" +
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
            EXTRACTOR
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
      jdbc.update("DELETE FROM User WHERE username = ?", id);
   }

   @Override
   public Class<SecurityUser> getEntityClass() {
      return SecurityUser.class;
   }
}
