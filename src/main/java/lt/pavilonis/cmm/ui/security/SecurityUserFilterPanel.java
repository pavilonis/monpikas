package lt.pavilonis.cmm.ui.security;

import com.vaadin.data.HasValue;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;

import java.util.Collections;
import java.util.List;

final class SecurityUserFilterPanel extends FilterPanel<SecurityUserFilter> {

   private TextField text;

   @Override
   protected List<HasValue<?>> getFields() {
      text = new TextField();
      return Collections.singletonList(text);
   }

   @Override
   public SecurityUserFilter getFilter() {
      return new SecurityUserFilter(null, text.getValue());
   }
}
