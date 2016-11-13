package lt.pavilonis.cmm;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
   public static String duration(LocalDateTime durationFrom) {
      LocalDateTime now = LocalDateTime.now();
      long days = durationFrom.until(now, ChronoUnit.DAYS);
      now.plusDays(days);

      long hours = durationFrom.until(now, ChronoUnit.HOURS);
      now.plusHours(hours);

      long minutes = durationFrom.until(now, ChronoUnit.MINUTES);
      now.plusMinutes(minutes);

      long seconds = durationFrom.until(now, ChronoUnit.SECONDS);
      now.plusSeconds(seconds);

      long ms = durationFrom.until(now, ChronoUnit.MILLIS);

      return (days > 0 ? days + "d " : "") +
            (hours > 0 ? hours + "h " : "") +
            (minutes > 0 ? minutes + "m " : "") +
            seconds + "." + ms / 100 + "s";
   }
}
