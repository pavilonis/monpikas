package lt.pavilonis.cmm.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemUserPasswordChangeService {

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   public void changePassword(long userId, String newPassword) {
      var sql = "UPDATE User SET password = :pass WHERE id = :id";
      jdbc.update(sql, Map.of("id", userId, "pass", passwordEncoder.encode(newPassword)));
   }
}
