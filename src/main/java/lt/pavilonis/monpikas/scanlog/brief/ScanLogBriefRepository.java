package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.data.provider.Query;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import lt.pavilonis.monpikas.common.util.TimeUtils;
import lt.pavilonis.monpikas.scanlog.ScanLogRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Slf4j
@Repository
public class ScanLogBriefRepository implements EntityRepository<ScanLogBrief, Void, ScanLogBriefFilter> {

   private final ScanLogRepository scanLogRepository;

   @Override
   public ScanLogBrief saveOrUpdate(ScanLogBrief entity) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public List<ScanLogBrief> load() {
      throw new IllegalStateException("Not implemented - not needed yet");
   }

   @Override
   public List<ScanLogBrief> load(ScanLogBriefFilter scanLogBriefFilter) {
      throw new IllegalStateException("Not implemented - not needed yet");
   }

   @Override
   public Optional<ScanLogBrief> find(Void aVoid) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public void delete(Void aVoid) {
      throw new IllegalStateException("Not implemented - not needed");
   }

   @Override
   public Class<ScanLogBrief> entityClass() {
      return ScanLogBrief.class;
   }

   @Override
   public Optional<SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter>> lazyDataProvider(ScanLogBriefFilter filter) {
      var provider = new SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter>() {
         @Override
         protected Stream<ScanLogBrief> fetchFromBackEnd(Query<ScanLogBrief, ScanLogBriefFilter> query) {
            LocalDateTime opStart = LocalDateTime.now();

            List<ScanLogBrief> result = scanLogRepository.loadBrief(
                  filter,
                  query.getSortOrders(),
                  QueryUtils.argOffset(query.getOffset()),
                  QueryUtils.argLimit(query.getLimit())
            );

            log("Loaded items " + query.getOffset() + " - " + query.getLimit(), result.size(), opStart);
            return result.stream();
         }

         @Override
         protected int sizeInBackEnd() {
            LocalDateTime opStart = LocalDateTime.now();
            int result = scanLogRepository.loadBriefSize(filter);
            log("Loaded size", result, opStart);
            return result;
         }

         private void log(String action, int size, LocalDateTime opStart) {
            log.info("{} [periodStart={}, periodEnd={}, text={}, scannerId={}, group={}, size={}, t={}]",
                  action,
                  filter.getPeriodStart(),
                  filter.getPeriodEnd(),
                  filter.getText(),
                  filter.getScannerId(),
                  filter.getRole(),
                  size,
                  TimeUtils.duration(opStart)
            );
         }
      };
      return Optional.of(provider);
   }
}
