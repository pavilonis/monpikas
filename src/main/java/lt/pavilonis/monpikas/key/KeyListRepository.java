package lt.pavilonis.monpikas.key;

import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.key.ui.KeyListFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class KeyListRepository implements EntityRepository<Key, Integer, KeyListFilter> {

   private final KeyRepository repository;

   public KeyListRepository(KeyRepository repository) {
      this.repository = repository;
   }

   @Override
   public Key saveOrUpdate(Key ignored) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<Key> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<Key> load(KeyListFilter filter) {
      String text = filter.getText();
      boolean filterByNumber = NumberUtils.isCreatable(text);
      Long scannerId = filter.getScannerId();

      if (filter.isLogMode()) {
         return repository.loadLog(
               filter.getPeriodStart(),
               filter.getPeriodEnd(),
               scannerId,
               filterByNumber ? Integer.parseInt(text) : null,
               null, //TODO add key action to filter
               filterByNumber ? null : text
         );
      }

      if (filterByNumber) {
         return repository.loadActive(scannerId, null, null, Integer.parseInt(text));
      }

      List<Key> result = repository.loadActive(scannerId, null, null, null);

      if (StringUtils.isBlank(text)) {
         return result;

      } else {
         return result.stream()
               .filter(key -> StringUtils.containsIgnoreCase(key.getUser().getName(), text))
               .collect(toList());
      }
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
