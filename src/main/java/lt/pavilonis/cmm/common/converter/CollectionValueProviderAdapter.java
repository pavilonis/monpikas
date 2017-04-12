package lt.pavilonis.cmm.common.converter;

import com.vaadin.data.ValueProvider;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionValueProviderAdapter<T> implements ValueProvider<T, String> {

   private final Function<T, Collection<?>> transformer;

   public CollectionValueProviderAdapter(Function<T, Collection<?>> transformer) {
      this.transformer = transformer;
   }

   @Override
   public String apply(T item) {
      String result = transformer.apply(item)
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));

      return result.length() <= 50
            ? result
            : result.substring(0, 49) + "...";
   }
}