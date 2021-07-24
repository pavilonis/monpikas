package lt.pavilonis.monpikas.common;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, ID, FILTER> {

   String ID = "id";

   T saveOrUpdate(T entity);

   List<T> load(FILTER filter);

   // Repository implementation should decide what default filter is
   List<T> load();

   Optional<T> find(ID id);

   void delete(ID id);

   Class<T> entityClass();

   default Optional<SizeConsumingBackendDataProvider<T, FILTER>> lazyDataProvider(FILTER filter) {
      return Optional.empty();
   }
}
