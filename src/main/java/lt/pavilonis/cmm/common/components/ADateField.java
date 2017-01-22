package lt.pavilonis.cmm.common.components;

import com.vaadin.data.util.converter.Converter;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.fields.MDateField;

import java.util.Date;

public class ADateField extends MDateField {
   public ADateField(Class clazz, String code) {

      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(clazz, code);

      this.setCaption(caption);
      this.setDateFormat("yyyy-MM-dd");
   }

   public ADateField withRequired(boolean required) {
      this.setRequired(true);
      return this;
   }

   public ADateField withConverter(Converter<Date, ?> converter) {
      this.setConverter(converter);
      return this;
   }
}
