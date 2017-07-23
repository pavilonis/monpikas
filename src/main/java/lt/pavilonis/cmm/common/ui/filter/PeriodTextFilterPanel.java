package lt.pavilonis.cmm.common.ui.filter;

import com.google.common.collect.Lists;
import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.field.ATextField;

import java.util.List;

public class PeriodTextFilterPanel extends PeriodFilterPanel<IdPeriodTextFilter> {

   private TextField text;

   @Override
   public IdPeriodTextFilter getFilter() {
      return new IdPeriodTextFilter(
            null,
            getPeriodStart().getValue(),
            getPeriodEnd().getValue(),
            text.getValue()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> fields = Lists.newArrayList(super.getFields());
      fields.add(text = new ATextField(PeriodTextFilterPanel.class, "text"));
      return fields;
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return text;
   }

   public TextField getText() {
      return text;
   }
}
