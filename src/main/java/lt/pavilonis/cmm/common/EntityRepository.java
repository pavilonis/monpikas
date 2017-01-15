package lt.pavilonis.cmm.common;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, ID> {

   T saveOrUpdate(T entity);

   List<T> loadAll();

   Optional<T> load(ID id);

   void delete(ID id);
}
