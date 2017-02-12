package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.ui.security.SecurityUserFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SecurityUserDetailsService implements UserDetailsService, EntityRepository<SecurityUser, String, SecurityUserFilter> {

   private static final SecurityUserResultSetExtractor EXTRACTOR = new SecurityUserResultSetExtractor();

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Override
   public UserDetails loadUserByUsername(String username) {
      return load(username)
            .orElse(null);
   }

   @Override
   @Transactional
   public SecurityUser saveOrUpdate(SecurityUser user) {
      if (StringUtils.isBlank(user.getUsername())) {
         throw new IllegalArgumentException("no username");
      }
      return load(user.getUsername()).isPresent()
            ? update(user)
            : save(user);
   }

   private SecurityUser update(SecurityUser user) {
      jdbc.update(
            "UPDATE User SET name = ?, email = ?, enabled = ? WHERE username = ?",
            user.getName(), user.getEmail(), user.isEnabled(), user.getUsername()
      );

      jdbc.update("DELETE FROM UserRole WHERE username = ?", user.getUsername());
      user.getAuthorities().forEach(authority -> jdbc.update(
            "INSERT INTO UserRole (username, name) VALUES (?, ?)",
            user.getUsername(), authority.getAuthority()
      ));

      return load(user.getUsername())
            .orElseThrow(IllegalStateException::new);
   }

   private SecurityUser save(SecurityUser user) {
      String temporaryPassword = passwordEncoder.encode(user.getUsername());
      jdbc.update(
            "INSERT INTO User (username, name, email, enabled, password) VALUES (?, ?, ?, ?, ?)",
            user.getUsername(), user.getName(), user.getEmail(), user.isEnabled(), temporaryPassword
      );
      return load(user.getUsername())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<SecurityUser> loadAll(SecurityUserFilter filter) {
      HashMap<String, Object> args = new HashMap<>();
      args.put("username", StringUtils.stripToNull(filter.getUsername()));
      args.put("text", StringUtils.isBlank(filter.getText()) ? null : "%" + filter.getText() + "%");
      return namedJdbc.query("" +
                  "SELECT u.*, r.name " +
                  "FROM User u " +
                  "  LEFT JOIN UserRole r ON r.username = u.username " +
                  "WHERE " +
                  "  (:username IS NULL OR :username = u.username)" +
                  "  AND (:text IS NULL OR u.name LIKE :text OR u.username LIKE :text) " +
                  "ORDER BY u.name",
            args,
            EXTRACTOR
      );
   }

   @Override
   public Optional<SecurityUser> load(String username) {
      List<SecurityUser> result = loadAll(new SecurityUserFilter(username, null));
      if (result.size() > 1) {
         throw new IllegalStateException();
      }

      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(String username) {
      jdbc.update("DELETE FROM User WHERE username = ?", username);
   }

   @Override
   public Class<SecurityUser> getEntityClass() {
      return SecurityUser.class;
   }
}
