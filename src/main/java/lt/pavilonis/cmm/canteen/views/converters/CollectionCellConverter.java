package lt.pavilonis.cmm.canteen.views.converters;

import com.vaadin.data.util.converter.Converter;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public class CollectionCellConverter implements Converter<String, Collection<?>> {
   @Override
   public Collection<?> convertToModel(String s, Class<? extends Collection<?>> aClass, Locale locale) throws ConversionException {
      return null;
   }

   @Override
   public String convertToPresentation(Collection<?> objects, Class<? extends String> aClass, Locale locale) throws ConversionException {
      return objects.stream().map(String::valueOf).collect(Collectors.joining(", "));
   }

   @Override
   public Class<Collection<?>> getModelType() {
      return null;
   }

   @Override
   public Class<String> getPresentationType() {
      return String.class;
   }
}
