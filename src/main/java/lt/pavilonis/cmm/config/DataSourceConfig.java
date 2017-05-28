package lt.pavilonis.cmm.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
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
   public JdbcTemplate jdbc() {
      return new JdbcTemplate(dataSourceLocal());
   }

   @Bean
   public NamedParameterJdbcTemplate jdbcNamed() {
      return new NamedParameterJdbcTemplate(dataSourceLocal());
   }

   @Bean
   public NamedParameterJdbcTemplate jdbcSalto() {
      return new NamedParameterJdbcTemplate(dataSourceSalto());
   }

   @Bean
   @Primary
   @ConfigurationProperties(prefix = "spring.datasource.local")
   public DataSource dataSourceLocal() {
      return DataSourceBuilder.create().build();
   }

   @Bean("dataSourceSalto")
   @ConfigurationProperties(prefix = "spring.datasource.salto")
   public DataSource dataSourceSalto() {
      return DataSourceBuilder.create().build();
   }

   @Bean
   public Flyway flywayLocal() {
      Flyway flyway = new Flyway();
      flyway.setValidateOnMigrate(false);
      flyway.setDataSource(dataSourceLocal());
      flyway.setLocations("classpath:db/migration/local");
      flyway.migrate();
      return flyway;
   }

   @Bean
   public Flyway flywaySalto() {
      Flyway flyway = new Flyway();
      flyway.setValidateOnMigrate(false);
      flyway.setDataSource(dataSourceSalto());
      flyway.setTable("mm_SchemaVersion");
      flyway.setLocations("classpath:db/migration/salto");
      flyway.migrate();
      return flyway;
   }
}
