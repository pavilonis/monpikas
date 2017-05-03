package lt.pavilonis.cmm.common.ui.filter;

import java.time.LocalDate;

public final class IdPeriodFilter {

   private final Long id;
   private final LocalDate periodStart;
   private final LocalDate periodEnd;

   private int offSet;
   private int limit;

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

   public int getOffSet() {
      return offSet;
   }

   public int getLimit() {
      return limit;
   }

   public IdPeriodFilter withOffset(int value) {
      this.offSet = value;
      return this;
   }

   public IdPeriodFilter withLimit(int value) {
      this.limit = value;
      return this;
   }
}
