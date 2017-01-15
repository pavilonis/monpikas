package lt.pavilonis.cmm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceAdapter {

   @Autowired
   private MessageSource messageSource;

   public String get(Object object, String propertyName) {
      String className = object instanceof Class
            ? ((Class) object).getSimpleName()
            : object.getClass().getSimpleName();
      return messageSource.getMessage(className + "." + propertyName, null, null);
   }
}
