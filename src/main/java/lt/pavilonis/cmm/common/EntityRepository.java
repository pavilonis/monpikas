package lt.pavilonis.cmm.common;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T, ID, FILTER> {

   T saveOrUpdate(T entity);

   List<T> load(FILTER filter);

   Optional<T> find(ID id);

   void delete(ID id);

   Class<T> getEntityClass();
}
