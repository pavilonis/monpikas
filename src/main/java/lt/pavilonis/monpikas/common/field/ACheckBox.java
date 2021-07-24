package lt.pavilonis.monpikas.common.field;

import com.vaadin.ui.CheckBox;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.service.MessageSourceAdapter;

public class ACheckBox extends CheckBox {
   public ACheckBox(Class clazz, String code) {
      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(clazz, code);

      this.setCaption(caption);
   }

   public ACheckBox withValue(boolean initialValue) {
      this.setValue(initialValue);
      return this;
   }
}
