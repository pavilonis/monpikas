package lt.pavilonis.monpikas.common.ui.filter;

import java.time.LocalDate;

public class IdPeriodTextFilter extends IdPeriodFilter {

   private final String text;

   public IdPeriodTextFilter(Long id, LocalDate periodStart, LocalDate periodEnd, String text) {
      super(id, periodStart, periodEnd);
      this.text = text;
   }

   public String getText() {
      return text;
   }
}
