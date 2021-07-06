package lt.pavilonis.cmm.school.scanlog;

import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogBrief;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogBriefFilter;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.common.util.TimeUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class ScanLogBriefRepository implements EntityRepository<ScanLogBrief, Void, ScanLogBriefFilter> {

   private final Logger logger = LoggerFactory.getLogger(getClass());
   private final ScanLogRepository scanLogRepository;

   public ScanLogBriefRepository(ScanLogRepository scanLogRepository) {
      this.scanLogRepository = scanLogRepository;
   }

   @Override
   public ScanLogBrief saveOrUpdate(ScanLogBrief entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<ScanLogBrief> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<ScanLogBrief> load(ScanLogBriefFilter scanLogBriefFilter) {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public Optional<ScanLogBrief> find(Void aVoid) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Void aVoid) {
      throw new NotImplementedException("Not needed");
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
            logger.info("{} [periodStart={}, periodEnd={}, text={}, scannerId={}, group={}, size={}, t={}]",
                  action,
                  DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodStart()),
                  filter.getPeriodEnd() == null
                        ? StringUtils.EMPTY
                        : DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodEnd()),
                  StringUtils.stripToEmpty(filter.getText()),
                  filter.getScannerId(),
                  StringUtils.stripToEmpty(filter.getRole()),
                  size,
                  TimeUtils.duration(opStart)
            );
         }
      };
      return Optional.of(provider);
   }
}
