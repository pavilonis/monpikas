package lt.pavilonis.cmm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemUserPasswordChangeService {

   private final PasswordEncoder passwordEncoder;
   private final NamedParameterJdbcTemplate jdbc;

   public SystemUserPasswordChangeService(PasswordEncoder passwordEncoder, NamedParameterJdbcTemplate jdbc) {
      this.passwordEncoder = passwordEncoder;
      this.jdbc = jdbc;
   }

   public void changePassword(long userId, String newPassword) {
      var sql = "UPDATE SystemUser SET password = :pass WHERE id = :id";
      jdbc.update(sql, Map.of("id", userId, "pass", passwordEncoder.encode(newPassword)));
   }
}
