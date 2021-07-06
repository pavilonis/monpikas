package lt.pavilonis.cmm.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableTransactionManagement
public class DataSourceConfig {

   @Autowired
   private DataSource dataSource;

   @Bean
   public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource);
   }

   @Bean
   public NamedParameterJdbcTemplate jdbc() {
      return new NamedParameterJdbcTemplate(dataSource);
   }

//   @Bean
//   @Primary
//   @ConfigurationProperties(prefix = "spring.datasource")
//   public DataSource dataSource() {
//      return DataSourceBuilder.create().build();
//   }

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
