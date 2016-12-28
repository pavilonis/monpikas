package lt.pavilonis.monpikas.server.views.converters;

import com.vaadin.data.util.converter.Converter;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.utils.Messages;

import java.util.Locale;

public class MealTypeCellConverter implements Converter<String, MealType> {
   @Override
   public MealType convertToModel(String value, Class<? extends MealType> targetType, Locale locale) throws ConversionException {
      return null;
   }

   @Override
   public String convertToPresentation(MealType value, Class<? extends String> targetType, Locale locale) throws ConversionException {
      return Messages.label("MealType." + value.toString());
   }

   @Override
   public Class<MealType> getModelType() {
      return null;
   }

   @Override
   public Class<String> getPresentationType() {
      return null;
   }
}
