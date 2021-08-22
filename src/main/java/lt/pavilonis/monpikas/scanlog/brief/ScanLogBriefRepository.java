package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.data.provider.Query;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import lt.pavilonis.monpikas.common.util.TimeUtils;
import lt.pavilonis.monpikas.scanlog.ScanLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class ScanLogBriefRepository implements EntityRepository<ScanLogBrief, Void, ScanLogBriefFilter> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ScanLogBriefRepository.class);
   private final ScanLogRepository scanLogRepository;

   public ScanLogBriefRepository(ScanLogRepository scanLogRepository) {
      this.scanLogRepository = scanLogRepository;
   }

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
      SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter> provider = new SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter>() {
         @Override
         protected Stream<ScanLogBrief> fetchFromBackEnd(Query<ScanLogBrief, ScanLogBriefFilter> query) {
            LocalDateTime opStart = LocalDateTime.now();

            List<ScanLogBrief> result = scanLogRepository.loadBrief(
                  filter,
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
            LOGGER.info("{} [periodStart={}, periodEnd={}, text={}, scannerId={}, group={}, size={}, t={}]",
                  action,
                  DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodStart()),
                  filter.getPeriodEnd() == null
                        ? ""
                        : DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodEnd()),
                  StringUtils.hasText(filter.getText()) ? filter.getText() : null,
                  filter.getScannerId(),
                  StringUtils.hasText(filter.getRole()) ? filter.getRole() : null,
                  size,
                  TimeUtils.duration(opStart)
            );
         }
      };
      return Optional.of(provider);
   }
}
