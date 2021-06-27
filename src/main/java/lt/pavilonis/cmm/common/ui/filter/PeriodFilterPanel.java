package lt.pavilonis.cmm.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.common.field.ADateField;

import java.util.List;

public class PeriodFilterPanel<T extends IdPeriodFilter> extends FilterPanel<T> {

   private DateField periodStart;
   private DateField periodEnd;

   @Override
   public T getFilter() {
      IdPeriodFilter idPeriodFilter = new IdPeriodFilter(
            periodStart.getValue(),
            periodEnd.getValue()
      );
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

   public DateField getPeriodStart() {
      return periodStart;
   }

   public DateField getPeriodEnd() {
      return periodEnd;
   }
}
