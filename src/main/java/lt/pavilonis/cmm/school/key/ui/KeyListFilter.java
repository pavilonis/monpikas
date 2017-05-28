package lt.pavilonis.cmm.school.key.ui;

import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;

import java.time.LocalDate;

public final class KeyListFilter extends IdPeriodFilter {

   private final Long scannerId;
   private final String text;
   private final boolean logMode;

   public KeyListFilter(LocalDate periodStart, LocalDate periodEnd,
                        Long scannerId, String text, boolean logMode) {
      super(periodStart, periodEnd);
      this.scannerId = scannerId;
      this.text = text;
      this.logMode = logMode;
   }

   public Long getScannerId() {
      return scannerId;
   }

   public boolean isLogMode() {
      return logMode;
   }

   public String getText() {
      return text;
   }
}
