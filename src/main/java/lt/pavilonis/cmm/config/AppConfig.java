package lt.pavilonis.cmm.config;

import com.mysql.jdbc.Driver;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Configuration
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
public class AppConfig {

   @Value("${api.auth.username}")
   private String apiUsername;

   @Value("${api.auth.password}")
   private String apiPassword;

   @Value("${jdbc.url:'jdbc:mysql://localhost:3306/monpikas'}")
   private String jdbcUrl;

   @Value("${jdbc.username}")
   private String jdbcUsername;

   @Value("${jdbc.password}")
   private String jdbcPassword;

   @Bean
   public RestTemplate restTemplate(RestTemplateBuilder builder) {
      return builder.basicAuthorization(apiUsername, apiPassword)
            .messageConverters(new MappingJackson2HttpMessageConverter())
            .build();
   }

   @Bean
   public ReloadableResourceBundleMessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setUseCodeAsDefaultMessage(true);
      messageSource.setBasename("classpath:lang/messages");
      messageSource.setCacheSeconds(0);
      messageSource.setDefaultEncoding("UTF-8");
      Locale.setDefault(new Locale("lt"));
      return messageSource;
   }

   @Bean
   public DataSource dataSource() {
      DataSource dataSource = new DataSource();
      dataSource.setDriverClassName(Driver.class.getName());
      dataSource.setUrl(jdbcUrl);
      dataSource.setUsername(jdbcUsername);
      dataSource.setPassword(jdbcPassword);
      return dataSource;
   }

   @Bean
   public JdbcTemplate jdbc() {
      return new JdbcTemplate(dataSource());
   }

   @Bean
   public NamedParameterJdbcTemplate namedJdbc() {
      return new NamedParameterJdbcTemplate(dataSource());
   }

   @Bean
   public Flyway flywayApi() {
      Flyway flyway = new Flyway();
      flyway.setDataSource(dataSource());
      flyway.setLocations("classpath:migration");
      flyway.setBaselineOnMigrate(true);
      flyway.migrate();
      return flyway;
   }
}
