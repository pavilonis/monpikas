package lt.pavilonis.cmm.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserPasswordChangeService {

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private JdbcTemplate jdbc;

   public void changePassword(long userId, String newPassword) {
      jdbc.update(
            "UPDATE User SET password = ? WHERE id = ?",
            passwordEncoder.encode(newPassword),
            userId
      );
   }
}
