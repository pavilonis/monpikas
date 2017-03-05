package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;

public class ATextField extends TextField {

   private final MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);

   public ATextField(Class clazz, String captionProperty) {
      String translateCaption = translate(clazz, captionProperty);
      setCaption(translateCaption);
   }

   protected String translate(Class clazz, String property) {
      return messageSource.get(clazz, property);
   }
}
