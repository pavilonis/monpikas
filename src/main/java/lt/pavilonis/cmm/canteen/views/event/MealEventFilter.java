package lt.pavilonis.cmm.canteen.views.event;


import lt.pavilonis.cmm.canteen.domain.PupilType;

import java.time.LocalDate;

public final class MealEventFilter {
   private final String text;
   private final LocalDate periodStart;
   private final LocalDate periodEnd;
   private final PupilType type;

   public MealEventFilter(String text, LocalDate periodStart, LocalDate periodEnd, PupilType type) {
      this.text = text;
      this.periodStart = periodStart;
      this.periodEnd = periodEnd;
      this.type = type;
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

   public PupilType getType() {
      return type;
   }
}