package lt.pavilonis.cmm.canteen.service;

import lt.pavilonis.cmm.canteen.domain.SpringSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

public class SpringSecurityUserDetailsService implements UserDetailsService {

   private static final ResultSetExtractor<SpringSecurityUser> RESULT_SET_EXTRACTOR = rs -> {
      String name = null;
      String username = null;
      String password = null;
      boolean enabled = false;

      boolean userDataCollected = false;
      Set<String> roles = new HashSet<>();
      while (rs.next()) {
         if (!userDataCollected) {
            name = rs.getString("u.name");
            username = rs.getString("u.username");
            password = rs.getString("u.password");
            enabled = rs.getBoolean("u.enabled");
            userDataCollected = true;
         }
         roles.add(rs.getString("r.name"));
      }

      if (!userDataCollected) {
         throw new UsernameNotFoundException("User not found");
      }

      return new SpringSecurityUser(name, username, password, enabled, roles);
   };

   @Autowired
   private JdbcTemplate jdbc;

   @Override
   public UserDetails loadUserByUsername(String username) {
      return jdbc.query(
            "SELECT u.*, r.name FROM User u JOIN UserRole r ON r.username = u.username WHERE u.username = ?",
            RESULT_SET_EXTRACTOR,
            username
      );
   }
}
