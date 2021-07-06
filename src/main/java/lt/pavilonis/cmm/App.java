package lt.pavilonis.cmm;

import lt.pavilonis.cmm.common.service.MessageSourceAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication
@EnableScheduling
public class App {

   public static ConfigurableApplicationContext context;
   private static MessageSourceAdapter messages;

   public static void main(String[] args) {
      context = SpringApplication.run(App.class);
      messages = context.getBean(MessageSourceAdapter.class);
   }

//   @Bean
//   public Filter characterEncodingFilter() {
//      var characterEncodingFilter = new CharacterEncodingFilter();
//      characterEncodingFilter.setEncoding(UTF_8.name());
//      characterEncodingFilter.setForceEncoding(true);
//      return characterEncodingFilter;
//   }

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
