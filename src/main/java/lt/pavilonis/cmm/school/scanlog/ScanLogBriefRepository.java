package lt.pavilonis.cmm.school.scanlog;

import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogBrief;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.util.QueryUtils;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class ScanLogBriefRepository implements EntityRepository<ScanLogBrief, Void, ScanLogBriefFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(ScanLogBriefRepository.class);

   private static final String FROM_WHERE_BLOCK = "" +
         "FROM mm_ScanLog sl " +
         "  JOIN mm_Scanner sc ON sc.id = sl.scanner_id " +
         "  JOIN tb_Cards c ON c.Cardcode IS NOT NULL AND c.ROMCode = sl.cardCode " +
         "  JOIN tb_Users u ON u.Cardcode = c.Cardcode " +
         "WHERE sl.dateTime >= :periodStart " +
         "  AND (:periodEnd IS NULL OR sl.dateTime <= :periodEnd) " +
         "  AND (:scannerId IS NULL OR sc.id = :scannerId) " +
         "  AND (:role IS NULL OR u.dummy4 = :role) " +
         "  AND (:text IS NULL OR sl.cardCode LIKE :text OR u.FirstName LIKE :text OR u.LastName LIKE :text)";

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Override
   public ScanLogBrief saveOrUpdate(ScanLogBrief entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<ScanLogBrief> load(ScanLogBriefFilter scanLogBriefFilter) {
      return null;
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
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter>> lazyDataProvider(ScanLogBriefFilter filter) {
      SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter> provider = new SizeConsumingBackendDataProvider<ScanLogBrief, ScanLogBriefFilter>() {
         @Override
         protected Stream<ScanLogBrief> fetchFromBackEnd(Query<ScanLogBrief, ScanLogBriefFilter> query) {
            LocalDateTime opStart = LocalDateTime.now();
            Map<String, Object> args = commonArgs(filter);
            args.put("argOffset", QueryUtils.argOffset(query.getOffset()));
            args.put("argLimit", QueryUtils.argLimit(query.getLimit()));

            List<ScanLogBrief> result = jdbcSalto.query("" +
                        "SELECT " +
                        "  sl.dateTime AS dateTime, " +
                        "  sl.cardCode AS cardCode, " +
                        "  sc.name AS scannerName, " +
                        "  CONCAT(u.FirstName, ' ', u.LastName) AS userName, " +
                        "  u.dummy3 AS userGroup, " +
                        "  u.dummy4 AS userRole " +
                        FROM_WHERE_BLOCK +
                        "ORDER BY sl.dateTime DESC " +
                        "OFFSET :argOffset ROWS FETCH NEXT :argLimit ROWS ONLY",
                  args,
                  new ScanLogBriefMapper()
            );

            log("Loaded items " + query.getOffset() + " - " + query.getLimit(), result.size(), opStart);

            return result.stream();
         }

         @Override
         protected int sizeInBackEnd() {
            LocalDateTime opStart = LocalDateTime.now();

            Integer result = jdbcSalto.queryForObject(
                  "SELECT COUNT(*) " + FROM_WHERE_BLOCK, commonArgs(filter), Integer.class);

            log("Loaded size", result, opStart);
            return result;
         }

         private void log(String action, int size, LocalDateTime opStart) {
            LOG.info("{} [periodStart={}, periodEnd={}, text={}, scannerId={}, group={}, size={}, duration={}]",
                  action,
                  DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodStart()),
                  filter.getPeriodEnd() == null
                        ? ""
                        : DateTimeFormatter.ISO_LOCAL_DATE.format(filter.getPeriodEnd()),
                  StringUtils.stripToEmpty(filter.getText()),
                  filter.getScanner() == null ? "" : filter.getScanner().getId(),
                  StringUtils.stripToEmpty(filter.getRole()),
                  size,
                  TimeUtils.duration(opStart)
            );
         }
      };
      return Optional.of(provider);
   }

   private Map<String, Object> commonArgs(ScanLogBriefFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());
      args.put("scannerId", filter.getScanner() == null ? null : filter.getScanner().getId());
      args.put("role", StringUtils.stripToNull(filter.getRole()));
      args.put("text", QueryUtils.likeArg(filter.getText()));
      return args;
   }
}
