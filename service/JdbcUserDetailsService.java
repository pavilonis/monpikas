package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JdbcUserDetailsService implements UserDetailsService {

   @Autowired
   private JdbcTemplate jdbc;

   @Override
   public UserDetails loadUserByUsername(String username) {
      return jdbc.query(
            "SELECT u.*, r.name FROM User u JOIN UserRole r ON r.username = u.username WHERE u.username = ?",
            rs -> {
               String name = null;
               String username1 = null;
               String password = null;
               boolean enabled = false;

               boolean userDataCollected = false;
               Set<String> roles = new HashSet<>();
               while (rs.next()) {
                  if (!userDataCollected) {
                     name = rs.getString("u.name");
                     username1 = rs.getString("u.username");
                     password = rs.getString("u.password");
                     enabled = rs.getBoolean("u.enabled");
                     userDataCollected = true;
                  }
                  roles.add(rs.getString("r.name"));
               }
               return userDataCollected
                     ? new User(name, username1, password, enabled, roles)
                     : null;
            },
            username
      );
   }
}
