package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.Converter;
import lt.pavilonis.monpikas.server.domain.PupilType;
import lt.pavilonis.monpikas.server.utils.Messages;

import java.util.Locale;

public class PupilTypeCellConverter implements Converter<String, PupilType> {
   @Override
   public PupilType convertToModel(String value, Class<? extends PupilType> targetType, Locale locale) throws ConversionException {
      return null;
   }

   @Override
   public String convertToPresentation(PupilType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return Messages.label("PupilType." + value.toString());
   }

   @Override
   public Class<PupilType> getModelType() {
      return null;
   }

   @Override
   public Class<String> getPresentationType() {
      return null;
   }
}
