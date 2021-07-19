package lt.pavilonis.cmm.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableTransactionManagement
public class DataSourceConfig {

   private final DataSource dataSource;

   public DataSourceConfig(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   @Bean
   public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource);
   }

   @Bean
   public NamedParameterJdbcTemplate jdbc() {
      return new NamedParameterJdbcTemplate(dataSource);
   }

   @Bean
   public Flyway flyway() {
      var config = new FluentConfiguration()
            .validateOnMigrate(true)
            .dataSource(dataSource)
            .locations("classpath:db/migration");

      var flyway = new Flyway(config);
      flyway.migrate();
      return flyway;
   }
}
