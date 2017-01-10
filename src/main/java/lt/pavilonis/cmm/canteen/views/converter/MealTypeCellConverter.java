package lt.pavilonis.cmm.canteen.views.converter;

import com.vaadin.data.util.converter.Converter;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.util.Messages;

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
