package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.Converter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
      return value == null
            ? new Date()
            : Date.from(value.atDate(now()).toInstant(ZoneOffset.ofHours(0)));
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
