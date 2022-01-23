package lt.pavilonis.monpikas.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.common.field.ATextField;

import java.util.ArrayList;
import java.util.List;

public class PeriodTextFilterPanel<T extends IdPeriodTextFilter> extends PeriodFilterPanel<T> {

   private TextField text;

   @Override
   public T getFilter() {
      IdPeriodTextFilter filter = IdPeriodTextFilter.builder()
            .periodStart(getPeriodStart().getValue())
            .periodEnd(getPeriodEnd().getValue())
            .text(text.getValue())
            .build();

      return (T) filter;
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
