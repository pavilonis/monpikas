package lt.pavilonis.cmm.common.field;

import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.fields.MTextField;

public class ATextField extends MTextField {

   private final MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);

   public ATextField(Class clazz, String captionProperty) {
      String translateCaption = translate(clazz, captionProperty);
      setCaption(translateCaption);
   }

   protected String translate(Class clazz, String property) {
      return messageSource.get(clazz, property);
   }

   public ATextField immediate(){
      this.setImmediate(true);
      return this;
   }
}
