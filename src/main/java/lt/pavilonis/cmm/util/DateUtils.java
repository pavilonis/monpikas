package lt.pavilonis.cmm.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.util.Date;

public class DateUtils {
   public static Date startOfDay(Date date) {
      return updateTime(date, LocalTime.MIN);
   }

   public static Date endOfDay(Date date) {
      return updateTime(date, LocalTime.MAX);
   }

   private static Date updateTime(Date date, TemporalAdjuster adjuster) {
      LocalDateTime localDateTime = dateToLocalDateTime(date);
      LocalDateTime updatedTime = localDateTime.with(adjuster);
      return localDateTimeToDate(updatedTime);
   }

   private static Date localDateTimeToDate(LocalDateTime dateTime) {
      Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
      return Date.from(instant);
   }

   private static LocalDateTime dateToLocalDateTime(Date date) {
      Instant instant = Instant.ofEpochMilli(date.getTime());
      return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
   }
}
