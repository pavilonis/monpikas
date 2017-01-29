package lt.pavilonis.cmm.common.field;

import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.fields.MCheckBox;

public class ACheckBox extends MCheckBox {
   public ACheckBox(Class clazz, String code) {
      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(clazz, code);

      this.setCaption(caption);
   }
}
