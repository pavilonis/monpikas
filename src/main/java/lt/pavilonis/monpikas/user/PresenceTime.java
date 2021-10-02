package lt.pavilonis.monpikas.user;

import lt.pavilonis.monpikas.common.Identified;

import java.time.LocalDate;
import java.time.LocalTime;

public class PresenceTime extends Identified<Void> {

   private final LocalDate date;
   private final LocalTime startTime;
   private final LocalTime endTime;
   private final double hourDifference;

   PresenceTime(LocalDate date, LocalTime startTime, LocalTime endTime, double hourDifference) {
      this.date = date;
      this.startTime = startTime;
      this.endTime = endTime;
      this.hourDifference = hourDifference;
   }

   public LocalDate getDate() {
      return date;
   }

   public LocalTime getStartTime() {
      return startTime;
   }

   public LocalTime getEndTime() {
      return endTime;
   }

   public double getHourDifference() {
      return hourDifference;
   }
}
