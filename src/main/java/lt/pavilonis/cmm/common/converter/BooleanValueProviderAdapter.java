package lt.pavilonis.cmm.common.converter;

import com.vaadin.data.ValueProvider;

import java.util.function.Function;

public class BooleanValueProviderAdapter<T> implements ValueProvider<T, String> {

   private final Function<T, Boolean> transformer;

   public BooleanValueProviderAdapter(Function<T, Boolean> transformer) {
      this.transformer = transformer;
   }

   @Override
   public String apply(T value) {

      return transformer.apply(value) == Boolean.TRUE
            ? "✔"
            : "";
   }
}
