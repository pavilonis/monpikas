package lt.pavilonis.cmm.classroom;

import java.time.LocalDate;

public final class ClassroomFilter {

   private final LocalDate periodStart;
   private final LocalDate periodEnd;
   private final String text;
   private final boolean isHistoryMode;

   public ClassroomFilter(LocalDate periodStart, LocalDate periodEnd, String text, boolean isHistoryMode) {
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
      this.text = text;
      this.isHistoryMode = isHistoryMode;
   }

   public LocalDate getPeriodStart() {
      return periodStart;
   }

   public LocalDate getPeriodEnd() {
      return periodEnd;
   }

   public String getText() {
      return text;
   }

   public boolean isHistoryMode() {
      return isHistoryMode;
   }
}
