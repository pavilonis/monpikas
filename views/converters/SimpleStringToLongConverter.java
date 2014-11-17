package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.StringToLongConverter;

import java.util.Locale;

public class SimpleStringToLongConverter extends StringToLongConverter {

   @Override
   public String convertToPresentation(Long value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return value.toString();
   }
}
