package lt.pavilonis.cmm.converter;

import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

   private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

   @Override
   public LocalDateTime convertToModel(String value, Class<? extends LocalDateTime> targetType, Locale locale) throws ConversionException {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public String convertToPresentation(LocalDateTime value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return DATE_FORMAT.format(value);
   }

   @Override
   public Class<LocalDateTime> getModelType() {
      return LocalDateTime.class;
   }

   @Override
   public Class<String> getPresentationType() {
      return String.class;
   }
}
