//package lt.pavilonis.cmm.converter;
//
//import com.vaadin.data.util.converter.Converter;
//import org.apache.commons.lang3.NotImplementedException;
//
//import java.util.Locale;
//
//public abstract class ToStringConverterAdapter<T> implements Converter<String, T> {
//
//   private final Class<T> clazz;
//
//   public ToStringConverterAdapter(Class<T> clazz) {
//      this.clazz = clazz;
//   }
//
//   @Override
//   public T convertToModel(String value, Class<? extends T> targetType, Locale locale) throws ConversionException {
//      throw new NotImplementedException("Not needed");
//   }
//
//   @Override
//   public String convertToPresentation(T value, Class<? extends String> targetType, Locale locale) throws ConversionException {
//      return toPresentation(value);
//   }
//
//   protected abstract String toPresentation(T model);
//
//   @Override
//   public Class<T> getModelType() {
//      return clazz;
//   }
//
//   @Override
//   public Class<String> getPresentationType() {
//      return String.class;
//   }
//}
