package lt.pavilonis.cmm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceAdapter {

   @Autowired
   private MessageSource messageSource;

   public String get(Object object, String propertyName) {
      String className;

      if (object instanceof Class) {
         Class clazz = (Class) object;

         className = clazz.isAnonymousClass()
               ? "ANONYMOUS"
               : clazz.getSimpleName();

      } else {
         className = object.getClass().getSimpleName();
      }
      return get(className + "." + propertyName);
   }

   public String get(String code) {
      return messageSource.getMessage(code, null, null);
   }
}
