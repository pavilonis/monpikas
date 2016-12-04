package lt.pavilonis.cmm.representation;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkTimeRepresentation {
   public final LocalDate date;
   public final LocalTime startTime;
   public final LocalTime endTime;
   public final double hourDifference;

   public WorkTimeRepresentation(LocalDate date, LocalTime startTime,
                                 LocalTime endTime, double hourDifference) {
      this.date = date;
      this.startTime = startTime;
      this.endTime = endTime;
      this.hourDifference = hourDifference;
   }
}
