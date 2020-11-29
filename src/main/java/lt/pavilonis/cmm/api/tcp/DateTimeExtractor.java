package lt.pavilonis.cmm.api.tcp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateTimeExtractor {

   private final Logger logger = LoggerFactory.getLogger(DateTimeExtractor.class.getSimpleName());
   private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
   private int counter = 1;

   LocalDateTime extract(String string) {
      List<String> charsToRemove = Arrays.asList(" ", "\"", ",", "\\n", "\n", "\\r", "\r");
      int index = string.indexOf(":") + 1;
      string = string.substring(index);
      for (String toRemove : charsToRemove) {
         string = string.replace(toRemove, StringUtils.EMPTY);
      }
      LocalDateTime result = parse(string);
      return result.plusNanos(getNanos() * 1000);
   }

   private long getNanos() {
      if (counter < 100) {
         return counter++;

      } else {
         return counter = 1;
      }
   }

   private LocalDateTime parse(String string) {
      try {
         return LocalDateTime.parse(string, dateTimeFormatter);
      } catch (DateTimeParseException e) {
         logger.error("Could not parse date: " + string);
         return LocalDateTime.now();
      }
   }
}
