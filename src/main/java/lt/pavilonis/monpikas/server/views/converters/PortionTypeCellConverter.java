package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.Converter;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;

import java.util.Locale;

public class PortionTypeCellConverter implements Converter<String, PortionType> {
   @Override
   public PortionType convertToModel(String value, Class<? extends PortionType> targetType, Locale locale) throws ConversionException {
      return null;
   }

   @Override
   public String convertToPresentation(PortionType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return value == PortionType.BREAKFAST
            ? "Pusryčiai"
            : "Pietus";
   }

   @Override
   public Class<PortionType> getModelType() {
      return null;
   }

   @Override
   public Class<String> getPresentationType() {
      return null;
   }
}
