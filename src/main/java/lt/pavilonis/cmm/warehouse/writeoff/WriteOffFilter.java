package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.time.LocalDate;

public class WriteOffFilter extends IdPeriodFilter {

   public WriteOffFilter(Long id, LocalDate periodStart, LocalDate periodEnd) {
      super(id, periodStart, periodEnd);
   }
}
