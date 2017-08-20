package lt.pavilonis.cmm.warehouse.receipt;

import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.time.LocalDate;

public class ReceiptFilter extends IdPeriodFilter {

   private final Supplier supplier;

   public ReceiptFilter(Long id, LocalDate periodStart, LocalDate periodEnd, Supplier supplier) {
      super(id, periodStart, periodEnd);
      this.supplier = supplier;
   }

   public Supplier getSupplier() {
      return supplier;
   }
}
