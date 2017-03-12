package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;

public class ATextField extends TextField {

   public ATextField(Class clazz, String captionProperty) {
      String translateCaption = translate(clazz, captionProperty);
      setCaption(translateCaption);
   }

   protected String translate(Class clazz, String property) {
      return App.translate(clazz, property);
   }

   public ATextField withRequired() {
      this.setRequiredIndicatorVisible(true);
      return this;
   }
}
