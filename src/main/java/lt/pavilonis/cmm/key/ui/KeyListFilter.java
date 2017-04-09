package lt.pavilonis.cmm.key.ui;

import java.time.LocalDate;

public final class KeyListFilter {
   private final LocalDate periodStart;
   private final LocalDate periodEnd;
   private final Long scannerId;
   private final String text;
   private final boolean logMode;

   public KeyListFilter() {
      this(null, null, null, null, false);
   }

   public KeyListFilter(LocalDate periodStart, LocalDate periodEnd, Long scannerId, String text, boolean logMode) {
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
      this.scannerId = scannerId;
      this.text = text;
      this.logMode = logMode;
   }

   public LocalDate getPeriodStart() {
      return periodStart;
   }

   public LocalDate getPeriodEnd() {
      return periodEnd;
   }

   public Long getScannerId() {
      return scannerId;
   }

   public String getText() {
      return text;
   }

   public boolean isLogMode() {
      return logMode;
   }
}
