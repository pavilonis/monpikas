package lt.pavilonis.monpikas.common.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import org.springframework.util.StringUtils;

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

      if (!StringUtils.hasText(value)) {
         return null;
      }

      return Integer.valueOf(value);
   }
}