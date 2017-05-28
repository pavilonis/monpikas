package lt.pavilonis.cmm.common.ui.filter;

import java.time.LocalDate;

public class IdPeriodFilter {

   private final Long id;
   private final LocalDate periodStart;
   private final LocalDate periodEnd;

   public IdPeriodFilter(long id) {
      this(id, null, null);
   }

   public IdPeriodFilter(LocalDate periodStart, LocalDate periodEnd) {
      this(null, periodStart, periodEnd);
   }

   public IdPeriodFilter(Long id, LocalDate periodStart, LocalDate periodEnd) {
      this.id = id;
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
   }

   public Long getId() {
      return id;
   }

   public LocalDate getPeriodStart() {
      return periodStart;
   }

   public LocalDate getPeriodEnd() {
      return periodEnd;
   }
}
