package db.migration;

import lt.pavilonis.cmm.canteen.domain.SystemUser;
import lt.pavilonis.cmm.security.service.SystemUserRepository;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V2021062716__Add_users_and_assign_roles implements SpringJdbcMigration {
   public void migrate(JdbcTemplate jdbc) throws Exception {
      var admin = "admin";
      var user = "user";
      var repo = new SystemUserRepository(new NamedParameterJdbcTemplate(jdbc), new BCryptPasswordEncoder());

      repo.saveOrUpdate(new SystemUser(null, null, admin, admin, null, true));
      repo.saveOrUpdate(new SystemUser(null, null, user, user, null, true));
      jdbc.update("INSERT INTO UserRole(user_id, role_id) " +
            "    SELECT u.id, r.id " +
            "    FROM SystemUser u " +
            "        JOIN Role r " +
            "    WHERE u.username = ?", admin);
   }
}