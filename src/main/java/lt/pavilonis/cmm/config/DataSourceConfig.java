package lt.pavilonis.cmm.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableTransactionManagement
public class DataSourceConfig {

   @Bean
   public DataSourceTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSourceLocal());
   }

   @Bean
   public NamedParameterJdbcTemplate jdbc() {
      return new NamedParameterJdbcTemplate(dataSourceLocal());
   }

   @Bean
   @Primary
   @ConfigurationProperties(prefix = "spring.datasource")
   public DataSource dataSourceLocal() {
      return DataSourceBuilder.create().build();
   }

   @Bean
   public Flyway flyway() {
      Flyway flyway = new Flyway();
      flyway.setValidateOnMigrate(false);
      flyway.setDataSource(dataSourceLocal());
      flyway.setLocations("classpath:db/migration");
      flyway.migrate();
      return flyway;
   }
}
