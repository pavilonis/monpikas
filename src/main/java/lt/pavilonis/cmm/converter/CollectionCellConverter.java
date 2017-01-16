package lt.pavilonis.cmm.converter;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionCellConverter extends ToStringConverterAdapter<Collection> {

   public CollectionCellConverter() {
      super(Collection.class);
   }

   @Override
   protected String toPresentation(Collection model) {
      return ((Collection<?>) model).stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
   }
}
