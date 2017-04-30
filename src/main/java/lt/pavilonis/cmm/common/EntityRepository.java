package lt.pavilonis.cmm.common;

import com.vaadin.data.provider.DataProvider;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, ID, FILTER> {

   String ID = "id";

   T saveOrUpdate(T entity);

   List<T> load(FILTER filter);

   Optional<T> find(ID id);

   void delete(ID id);

   Class<T> entityClass();

   default Optional<DataProvider<T, FILTER>> lazyDataProvider() {
      return Optional.empty();
   }
}
