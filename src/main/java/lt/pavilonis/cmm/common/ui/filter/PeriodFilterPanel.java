package lt.pavilonis.cmm.common.ui.filter;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.common.field.ADateField;

import java.util.Arrays;
import java.util.List;

public class PeriodFilterPanel extends FilterPanel<IdPeriodFilter> {

   private DateField periodStart;
   private DateField periodEnd;

   @Override
   public IdPeriodFilter getFilter() {
      return new IdPeriodFilter(
            periodStart.getValue(),
            periodEnd.getValue()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      periodStart = new ADateField(getClass(), "periodStart");
      periodEnd = new ADateField(getClass(), "periodEnd");
      return Arrays.asList(periodStart, periodEnd);
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return periodStart;
   }
}
