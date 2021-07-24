package lt.pavilonis.monpikas.scanlog.brief;

import lt.pavilonis.monpikas.common.ui.filter.IdPeriodFilter;

import java.time.LocalDate;

public final class ScanLogBriefFilter extends IdPeriodFilter {

   private final String text;
   private final Long scannerId;
   private final String role;

   public ScanLogBriefFilter(LocalDate periodStart, LocalDate periodEnd,
                             String text, Long scannerId, String role) {

      super(periodStart, periodEnd);
      this.text = text;
      this.scannerId = scannerId;
      this.role = role;
   }

   public String getText() {
      return text;
   }

   public Long getScannerId() {
      return scannerId;
   }

   public String getRole() {
      return role;
   }
}
