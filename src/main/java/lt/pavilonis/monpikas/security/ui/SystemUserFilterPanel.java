package lt.pavilonis.monpikas.security.ui;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;

import java.util.Collections;
import java.util.List;

final class SystemUserFilterPanel extends FilterPanel<SystemUserFilter> {

   private TextField text;

   @Override
   protected List<HasValue<?>> getFields() {
      text = new TextField();
      return Collections.singletonList(text);
   }

   @Override
   public SystemUserFilter getFilter() {
      return new SystemUserFilter(null, null, text.getValue());
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return text;
   }
}
