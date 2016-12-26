package lt.pavilonis.cmm.converter;

import com.vaadin.data.util.converter.Converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class LocalDateConverter implements Converter<Date, LocalDate> {

   @Override
   public LocalDate convertToModel(Date value, Class<? extends LocalDate> targetType, Locale locale) throws ConversionException {
      return value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
   }

   @Override
   public Date convertToPresentation(LocalDate value, Class<? extends Date> targetType, Locale locale) throws ConversionException {
      return Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
   }

   @Override
   public Class<Date> getPresentationType() {
      return Date.class;
   }

   @Override
   public Class<LocalDate> getModelType() {
      return LocalDate.class;
   }
}
