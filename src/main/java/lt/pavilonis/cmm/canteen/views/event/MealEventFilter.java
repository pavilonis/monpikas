package lt.pavilonis.cmm.canteen.views.event;

import org.joda.time.LocalDateTime;

import java.util.Date;

public final class MealEventFilter {
   private final String text;
   private final Date periodStart;
   private final Date periodEnd;

   public MealEventFilter(String text, Date periodStart, Date periodEnd) {
      this.text = text;
      this.periodStart = periodStart == null
            ? null :
            LocalDateTime.fromDateFields(periodStart).withTime(0, 0, 0, 0).toDate();
      this.periodEnd = periodEnd == null
            ? null
            : LocalDateTime.fromDateFields(periodEnd).withTime(23, 59, 59, 999).toDate();

   }

   public String getText() {
      return text;
   }

   public Date getPeriodStart() {
      return periodStart;
   }

   public Date getPeriodEnd() {
      return periodEnd;
   }
}