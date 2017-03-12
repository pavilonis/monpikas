package lt.pavilonis.cmm.converter;

import com.vaadin.data.ValueProvider;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionValueProviderAdapter<T> implements ValueProvider<T, String> {

   private final Function<T, Collection<?>> collectionSupplier;

   public CollectionValueProviderAdapter(Function<T, Collection<?>> collectionSupplier) {
      this.collectionSupplier = collectionSupplier;
   }

   @Override
   public String apply(T item) {
      return collectionSupplier.apply(item)
            .stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
   }
}
