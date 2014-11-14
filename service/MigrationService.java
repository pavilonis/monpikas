package lt.pavilonis.monpikas.server.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Service
public class MigrationService {

   @Autowired
   private DataSource monpikasMigrationDataSource;

   @PostConstruct
   public void migrate() {
      Flyway flyway = new Flyway();
      flyway.setDataSource(monpikasMigrationDataSource);
      flyway.setInitOnMigrate(true);
      flyway.migrate();
   }
}
