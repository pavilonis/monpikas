package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.ui.security.SecurityUserFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SecurityUserDetailsService implements UserDetailsService, EntityRepository<SecurityUser, String, SecurityUserFilter> {

   private static final SecurityUserResultSetExtractor EXTRACTOR = new SecurityUserResultSetExtractor();

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public UserDetails loadUserByUsername(String username) {
      return load(username)
            .orElse(null);
   }

   @Override
   public SecurityUser saveOrUpdate(SecurityUser entity) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public List<SecurityUser> loadAll(SecurityUserFilter filter) {
      HashMap<String, Object> args = new HashMap<>();
      args.put("username", StringUtils.stripToNull(filter.getUsername()));
      args.put("text", StringUtils.isBlank(filter.getText()) ? null : "%" + filter.getText() + "%");
      return jdbc.query("" +
                  "SELECT u.*, r.name " +
                  "FROM User u " +
                  "  JOIN UserRole r ON r.username = u.username " +
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
   public void delete(String s) {
      throw new NotImplementedException("not needed yet");
   }

   @Override
   public Class<SecurityUser> getEntityClass() {
      return SecurityUser.class;
   }
}
