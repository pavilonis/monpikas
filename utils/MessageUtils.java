package lt.pavilonis.monpikas.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

   @Autowired
   private Environment source;

   //private static final ResourceBundle properties = ResourceBundle.getBundle("messages.properties");

   public String getValue(String key) {
      return source.getProperty(key);
   }
}
