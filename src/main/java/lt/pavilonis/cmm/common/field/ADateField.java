package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;

import java.time.LocalDate;

public class ADateField extends DateField {

   public ADateField(Class clazz, String code) {

      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(clazz, code);

      this.setCaption(caption);
      this.setDateFormat("yyyy-MM-dd");
   }

   public ADateField withRequired() {
      this.setRequiredIndicatorVisible(true);
      return this;
   }

   public ADateField withValue(LocalDate value) {
      this.setValue(value);
      return this;
   }
}
