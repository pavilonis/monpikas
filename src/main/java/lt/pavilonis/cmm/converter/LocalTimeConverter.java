package lt.pavilonis.cmm.converter;

import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeConverter implements Converter<String, LocalTime> {

   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

   @Override
   public LocalTime convertToModel(String value, Class<? extends LocalTime> targetType, Locale locale) throws ConversionException {
      throw new NotImplementedException("not needed");
   }

   @Override
   public String convertToPresentation(LocalTime value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return TIME_FORMAT.format(value);
   }

   @Override
   public Class<LocalTime> getModelType() {
      return LocalTime.class;
   }

   @Override
   public Class<String> getPresentationType() {
      return String.class;
   }
}
