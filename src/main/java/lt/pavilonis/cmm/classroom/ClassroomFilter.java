package lt.pavilonis.cmm.classroom;

import java.time.LocalDate;

public final class ClassroomFilter {

   private final LocalDate periodStart;
   private final LocalDate periodEnd;
   private final String text;
   private final boolean logMode;

   public ClassroomFilter(LocalDate periodStart, LocalDate periodEnd, String text, boolean logMode) {
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
      this.text = text;
      this.logMode = logMode;
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

   public boolean isLogMode() {
      return logMode;
   }
}
