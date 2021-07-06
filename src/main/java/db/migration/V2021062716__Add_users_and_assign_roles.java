package db.migration;

import lt.pavilonis.cmm.security.SystemUser;
import lt.pavilonis.cmm.security.service.SystemUserRepository;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

public class V2021062716__Add_users_and_assign_roles extends BaseJavaMigration {

   @Override
   public void migrate(Context context) {
      var jdbc = new NamedParameterJdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
      var admin = "admin";
      var user = "user";
      var repo = new SystemUserRepository(jdbc, new BCryptPasswordEncoder());
      var sql = "INSERT INTO UserRole(user_id, role_id) " +
            "    SELECT u.id, r.id " +
            "    FROM SystemUser u " +
            "        JOIN Role r " +
            "    WHERE u.username = :admin";

      repo.saveOrUpdate(new SystemUser(null, null, admin, admin, null, true));
      repo.saveOrUpdate(new SystemUser(null, null, user, user, null, true));
      jdbc.update(sql, Map.of("admin", admin));
   }

}