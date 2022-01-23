package lt.pavilonis.monpikas.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import lombok.Getter;
import lt.pavilonis.monpikas.common.field.ADateField;

import java.util.List;

@Getter
public class PeriodFilterPanel<T extends IdPeriodFilter> extends FilterPanel<T> {

   private DateField periodStart;
   private DateField periodEnd;

   @Override
   public T getFilter() {
      var idPeriodFilter = IdPeriodTextFilter.builder()
            .periodStart(periodStart.getValue())
            .periodEnd(periodEnd.getValue())
            .build();

      return (T) idPeriodFilter;
   }

   @Override
   protected List<HasValue<?>> getFields() {
      periodStart = new ADateField(PeriodFilterPanel.class, "periodStart");
      periodEnd = new ADateField(PeriodFilterPanel.class, "periodEnd");
      return List.of(periodStart, periodEnd);
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return periodStart;
   }
}
