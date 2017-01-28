package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2017012801__Hash_User_Passwords implements SpringJdbcMigration {
   public void migrate(JdbcTemplate jdbc) throws Exception {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      jdbc.queryForList("SELECT password FROM User", String.class)
            .forEach(password -> jdbc.update(
                  "UPDATE User SET password = ? WHERE password = ?",
                  encoder.encode(password), password
            ));
   }
}