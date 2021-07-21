package lt.pavilonis.cmm;

import lt.pavilonis.cmm.common.service.MessageSourceAdapter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication
@EnableScheduling
public class App {

   public static ConfigurableApplicationContext context;
   private static MessageSourceAdapter messages;
   private final DataSource dataSource;

   public App(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public static void main(String[] args) {
      context = SpringApplication.run(App.class);
      messages = context.getBean(MessageSourceAdapter.class);
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

   @Bean
   public ReloadableResourceBundleMessageSource messageSource() {
      var messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setUseCodeAsDefaultMessage(true);
      messageSource.setBasename("classpath:lang/messages");
      messageSource.setCacheSeconds(0);
      messageSource.setDefaultEncoding(UTF_8.name());
      Locale.setDefault(new Locale("lt"));
      return messageSource;
   }

   public static String translate(Object objectOrClass, String property) {
      return messages.get(objectOrClass, property);
   }

   public static String translate(String property) {
      return messages.get(property);
   }
}
