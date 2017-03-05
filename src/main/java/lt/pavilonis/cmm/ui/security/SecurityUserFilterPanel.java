package lt.pavilonis.cmm.ui.security;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;

import java.util.Collections;
import java.util.List;

public class SecurityUserFilterPanel extends FilterPanel<SecurityUserFilter> {

   private TextField text;

   @Override
   protected List<AbstractField<?>> getFields() {
      text = new TextField();
      return Collections.singletonList(text);
   }

   @Override
   public SecurityUserFilter getFilter() {
      return new SecurityUserFilter(null, text.getValue());
   }
}
