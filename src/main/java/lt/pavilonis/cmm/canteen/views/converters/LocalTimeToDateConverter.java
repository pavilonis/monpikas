package lt.pavilonis.cmm.canteen.views.converters;

import com.vaadin.data.util.converter.Converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.time.LocalDate.now;

public class LocalTimeToDateConverter implements Converter<Date, LocalTime> {
   @Override
   public LocalTime convertToModel(Date value, Class<? extends LocalTime> targetType, Locale locale) throws ConversionException {
      return LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()).toLocalTime();
   }

   @Override
   public Date convertToPresentation(LocalTime value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
      if (value == null) {
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.HOUR, 0);
         calendar.set(Calendar.MINUTE, 0);
         calendar.set(Calendar.SECOND, 0);
         calendar.set(Calendar.MILLISECOND, 0);
         return new Date(calendar.getTimeInMillis());
      }
      Instant instant = now().atTime(value).atZone(ZoneId.systemDefault()).toInstant();
      return Date.from(instant);
   }

   @Override
   public Class<LocalTime> getModelType() {
      return LocalTime.class;
   }

   @Override
   public Class<Date> getPresentationType() {
      return Date.class;
   }
}
