package lt.pavilonis.monpikas.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.common.field.ATextField;

import java.util.ArrayList;
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
      List<HasValue<?>> fields = new ArrayList<>(super.getFields());
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
