package lt.pavilonis.cmm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {

   public static ConfigurableApplicationContext context;

   public static void main(String[] args) {
      context = SpringApplication.run(App.class);
   }
}
