package lt.pavilonis.monpikas.key;

import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.key.ui.KeyListFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public List<Key> load() {
      throw new IllegalStateException("Not implemented - not needed yet");
   }

   @Override
   public List<Key> load(KeyListFilter filter) {
      String text = filter.getText();
      Integer number = parseNumber(text);
      Long scannerId = filter.getScannerId();

      if (filter.isLogMode()) {
         return repository.loadLog(
               filter.getPeriodStart(),
               filter.getPeriodEnd(),
               scannerId,
               number,
               null, //TODO add key action to filter
               number == null ? text : null
         );
      }

      if (number != null) {
         return repository.loadActive(scannerId, null, null, number);
      }

      List<Key> result = repository.loadActive(scannerId, null, null, null);
      if (StringUtils.hasText(text)) {
         var lowerCaseText = text.toLowerCase();
         return result.stream()
               .filter(key -> key.getUser().getName().toLowerCase().contains(lowerCaseText))
               .collect(toList());

      } else {
         return result;
      }
   }

   private Integer parseNumber(String text) {
      if (!StringUtils.hasText(text)) {
         return null;
      }
      try {
         return Integer.parseInt(text);
      } catch (NumberFormatException e) {
         return null;
      }
   }

   @Override
   public Optional<Key> find(Integer ignored) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public void delete(Integer ignored) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public Class<Key> entityClass() {
      return Key.class;
   }
}
