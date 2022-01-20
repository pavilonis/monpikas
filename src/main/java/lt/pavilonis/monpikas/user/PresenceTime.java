package lt.pavilonis.monpikas.user;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lt.pavilonis.monpikas.common.Identified;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = false)
@Value
public class PresenceTime extends Identified<Void> {

   LocalDate date;
   LocalTime startTime;
   LocalTime endTime;
   double hourDifference;

}
