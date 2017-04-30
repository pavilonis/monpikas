package lt.pavilonis.cmm.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;

import java.util.Collections;
import java.util.List;

public class NameFilterPanel extends FilterPanel<IdNameFilter> {

   private TextField textField;

   @Override
   public IdNameFilter getFilter() {
      return new IdNameFilter(textField.getValue());
   }

   @Override
   protected List<HasValue<?>> getFields() {
      textField = new TextField();
      return Collections.singletonList(textField);
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
