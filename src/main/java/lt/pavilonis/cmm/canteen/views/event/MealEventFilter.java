package lt.pavilonis.cmm.canteen.views.event;


import java.time.LocalDate;

public final class MealEventFilter {
   private final String text;
   private final LocalDate periodStart;
   private final LocalDate periodEnd;

   public MealEventFilter(String text, LocalDate periodStart, LocalDate periodEnd) {
      this.text = text;
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;

   }

   public String getText() {
      return text;
   }

   public LocalDate getPeriodStart() {
      return periodStart;
   }

   public LocalDate getPeriodEnd() {
      return periodEnd;
   }
}