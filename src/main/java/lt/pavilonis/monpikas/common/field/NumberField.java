package lt.pavilonis.monpikas.common.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class NumberField extends CustomField<Integer> {

   private final TextField textField = new TextField();

   @Override
   protected Component initContent() {
      return textField;
   }

   @Override
   protected void doSetValue(Integer value) {
      textField.setValue(value == null ? null : value.toString());
   }

   @Override
   public Integer getValue() {
      String value = textField.getValue();

      if (StringUtils.isBlank(value)) {
         return null;

      } else if (NumberUtils.isDigits(value)) {
         return Integer.valueOf(value);

      } else {
         throw new RuntimeException("Could not parse integer value: " + value);
      }
   }
}