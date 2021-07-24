package lt.pavilonis.monpikas.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;

import java.util.Collections;
import java.util.List;

public class NameFilterPanel extends FilterPanel<IdTextFilter> {

   protected TextField textField;

   @Override
   public IdTextFilter getFilter() {
      return new IdTextFilter(textField.getValue());
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
