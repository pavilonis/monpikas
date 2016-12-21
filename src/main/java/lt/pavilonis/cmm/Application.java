package lt.pavilonis.cmm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@SpringBootApplication
public class Application {

   @Value("${api.auth.username}")
   private String apiUsername;

   @Value("${api.auth.password}")
   private String apiPassword;

   public static ConfigurableApplicationContext context;

   public static void main(String[] args) {
      context = SpringApplication.run(Application.class);
   }

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

}
