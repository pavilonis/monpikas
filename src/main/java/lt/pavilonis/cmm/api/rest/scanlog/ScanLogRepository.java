package lt.pavilonis.cmm.api.rest.scanlog;

import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.key.KeyRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.common.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class ScanLogRepository {

   private static final Logger LOG = LoggerFactory.getLogger(ScanLogRepository.class.getSimpleName());
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM ScanLog sl " +
         "  JOIN Scanner sc ON sc.id = sl.scanner_id " +
         "  JOIN User u ON u.cardCode = sl.cardCode " +
         "WHERE sl.dateTime >= :periodStart " +
         "  AND (:periodEnd IS NULL OR sl.dateTime <= :periodEnd) " +
         "  AND (:scannerId IS NULL OR sc.id = :scannerId) " +
         "  AND (:role IS NULL OR u.organizationRole = :role) " +
         "  AND (:text IS NULL OR sl.cardCode LIKE :text OR u.name LIKE :text)";

   private final NamedParameterJdbcTemplate jdbc;
   private final KeyRepository keyRepository;
   private final UserRepository userRepository;

   public ScanLogRepository(NamedParameterJdbcTemplate jdbc, KeyRepository keyRepository, UserRepository userRepository) {
      this.jdbc = jdbc;
      this.keyRepository = keyRepository;
      this.userRepository = userRepository;
   }

   public ScanLog saveCheckedAndLoad(long scannerId, String cardCode) {
      Long scanLogId = saveChecked(scannerId, cardCode);

      return scanLogId == null
            ? null
            : loadById(scannerId, scanLogId);
   }

   private ScanLog loadById(long scannerId, long scanLogId) {
      return jdbc.queryForObject(
            "SELECT cardCode, dateTime FROM ScanLog WHERE id = :id",
            Map.of("id", scanLogId),
            (rs, i) -> {
               String loadedCardCode = rs.getString(1);
               User user = userRepository.load(loadedCardCode, true);
               List<Key> keys = keyRepository.loadActive(scannerId, loadedCardCode, null);
               return new ScanLog(rs.getTimestamp(2).toLocalDateTime(), user, keys);
            }
      );
   }

   public Long saveChecked(long scannerId, String cardCode) {

      if (!userRepository.exists(cardCode)) {
         LOG.warn("Skipping scan log: user not found [scannerId={}, cardCode={}]", scannerId, cardCode);
         return null;
      }

      var args = Map.of("cardCode", cardCode, "scannerId", scannerId);
      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO ScanLog (cardCode, scanner_id) VALUES (:cardCode, :scannerId)";

      jdbc.update(sql, new MapSqlParameterSource(args), keyHolder);
      LOG.info("ScanLog saved");
      return keyHolder.getKey().longValue();
   }

   public int loadBriefSize(ScanLogBriefFilter filter) {
      return jdbc.queryForObject("SELECT COUNT(*) " + FROM_WHERE_BLOCK, commonArgs(filter), Integer.class);
   }

   public List<ScanLogBrief> loadBrief(ScanLogBriefFilter filter, long offset, long limit) {
      Map<String, Object> args = commonArgs(filter);
      args.put("argOffset", offset);
      args.put("argLimit", limit);

      var sql = "SELECT " +
            "  sl.dateTime, " +
            "  sl.cardCode," +
            "  sc.name AS scannerName, " +
            "  u.name, " +
            "  u.organizationGroup, " +
            "  u.organizationRole " +
            FROM_WHERE_BLOCK +
            "ORDER BY sl.dateTime DESC " +
            "LIMIT :argLimit OFFSET :argOffset";

      return jdbc.query(sql, args, new ScanLogBriefMapper());
   }

   private Map<String, Object> commonArgs(ScanLogBriefFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());
      args.put("scannerId", filter.getScannerId());
      args.put("role", StringUtils.stripToNull(filter.getRole()));
      args.put("text", QueryUtils.likeArg(filter.getText()));
      return args;
   }
}
