package lt.pavilonis.cmm.school.key;

import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.key.KeyRepository;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.school.key.ui.KeyListFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class KeyListRepository implements EntityRepository<Key, Integer, KeyListFilter> {

   @Autowired
   private KeyRepository repository;

   @Override
   public Key saveOrUpdate(Key ignored) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<Key> load(KeyListFilter filter) {
      boolean number = NumberUtils.isNumber(filter.getText());
      if (filter.isLogMode()) {
         return repository.loadLog(
               filter.getPeriodStart(),
               filter.getPeriodEnd(),
               filter.getScannerId(),
               number ? Integer.parseInt(filter.getText()) : null,
               null, //TODO add key action to filter
               number ? null : filter.getText()
         );
      }
      return repository.loadAssigned(filter.getScannerId(), null, integerOrNUll(filter.getText()));
   }

   protected Integer integerOrNUll(String value) {
      return NumberUtils.isNumber(value)
            ? Integer.parseInt(value)
            : null;
   }

   @Override
   public Optional<Key> find(Integer ignored) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Integer ignored) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<Key> entityClass() {
      return Key.class;
   }
}
