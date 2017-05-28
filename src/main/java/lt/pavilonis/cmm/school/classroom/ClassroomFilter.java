package lt.pavilonis.cmm.school.classroom;

import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;

import java.time.LocalDate;

public final class ClassroomFilter extends IdPeriodFilter {

   private final String text;
   private final boolean isHistoryMode;

   public ClassroomFilter(LocalDate periodStart, LocalDate periodEnd, String text, boolean isHistoryMode) {
      super(periodStart, periodEnd);
      this.text = text;
      this.isHistoryMode = isHistoryMode;
   }

   public String getText() {
      return text;
   }

   public boolean isHistoryMode() {
      return isHistoryMode;
   }
}
