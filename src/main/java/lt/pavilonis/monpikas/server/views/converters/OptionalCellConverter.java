package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.Converter;

import java.util.Locale;
import java.util.Optional;

public class OptionalCellConverter implements Converter<String, Optional<?>> {
   @Override
   public Optional<?> convertToModel(String value, Class<? extends Optional<?>> targetType, Locale locale) throws ConversionException {
      return null;
   }

   @Override
   public String convertToPresentation(Optional<?> value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return value.map(String::valueOf).orElse("");
   }

   @Override
   public Class<Optional<?>> getModelType() {
      return null;
   }

   @Override
   public Class<String> getPresentationType() {
      return String.class;
   }
}
