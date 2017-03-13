package lt.pavilonis.cmm.common;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;

import java.util.Collections;
import java.util.List;

public final class HiddenFilterPanel<F> extends FilterPanel<F> {

   public HiddenFilterPanel() {
      setVisible(false);
      setEnabled(false);
   }

   @Override
   public F getFilter() {
      return null;
   }

   @Override
   protected List<HasValue<?>> getFields() {
      return Collections.emptyList();
   }
}
