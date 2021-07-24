package lt.pavilonis.monpikas.scanlog;

import lt.pavilonis.monpikas.key.Key;
import lt.pavilonis.monpikas.key.KeyRepository;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBrief;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBriefFilter;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBriefMapper;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.user.UserRepository;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static lt.pavilonis.monpikas.user.UserRepository.USER_MAPPER;

@Repository
public class ScanLogRepository {

   private static final Logger LOGGER = LoggerFactory.getLogger(ScanLogRepository.class);
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM ScanLog sl " +
         "  JOIN Scanner sc ON sc.id = sl.scanner_id " +
         "  JOIN User u ON u.id = sl.user_id " +
         "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +
         "WHERE sl.dateTime >= :periodStart " +
         "  AND (:periodEnd IS NULL OR sl.dateTime <= :periodEnd) " +
         "  AND (:scannerId IS NULL OR sc.id = :scannerId) " +
         "  AND (:role IS NULL OR u.organizationRole = :role) " +
         "  AND (:text IS NULL OR u.cardCode LIKE :text OR u.name LIKE :text)";

   private final NamedParameterJdbcTemplate jdbc;
   private final KeyRepository keyRepository;
   private final UserRepository userRepository;

   public ScanLogRepository(NamedParameterJdbcTemplate jdbc, KeyRepository keyRepository, UserRepository userRepository) {
      this.jdbc = jdbc;
      this.keyRepository = keyRepository;
      this.userRepository = userRepository;
   }

   public ScanLog store(long scannerId, String cardCode) {
      return save(scannerId, cardCode)
            .map(scanLogId -> loadById(scannerId, scanLogId))
            .orElse(null);
   }

   private ScanLog loadById(long scannerId, long scanLogId) {
      return jdbc.queryForObject(
            "SELECT " +
                  "  sl.dateTime, " +
                  "  u.id, " +
                  "  u.cardCode, " +
                  "  u.name, " +
                  "  u.birthDate, " +
                  "  u.organizationGroup, " +
                  "  u.organizationRole, " +
                  "  u.photo, " +
                  "  supervisor.id AS supervisorId, " +
                  "  supervisor.name AS supervisorName " +
                  "FROM ScanLog sl " +
                  "  JOIN User u ON u.id = sl.user_id " +
                  "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +
                  "WHERE sl.id = :id",
            Map.of("id", scanLogId),
            (rs, i) -> {
               User user = USER_MAPPER.mapRow(rs);
               List<Key> keys = keyRepository.loadActive(scannerId, rs.getLong("id"), null, null);
               return new ScanLog(rs.getTimestamp("dateTime").toLocalDateTime(), user, keys);
            }
      );
   }

   private Optional<Long> save(long scannerId, String cardCode) {

      User user = userRepository.load(cardCode, false);
      if (user == null) {
         LOGGER.warn("Skipping scan log: user not found [scannerId={}, cardCode={}]", scannerId, cardCode);
         return Optional.empty();
      }

      var args = Map.of("userId", user.getId(), "scannerId", scannerId);
      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO ScanLog (user_id, scanner_id) VALUES (:userId, :scannerId)";

      jdbc.update(sql, new MapSqlParameterSource(args), keyHolder);
      LOGGER.info("ScanLog saved");
      return Optional.of(keyHolder.getKey().longValue());
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
            "  sc.name AS scannerName, " +
            "  u.name, " +
            "  u.cardCode," +
            "  u.organizationGroup, " +
            "  u.organizationRole, " +
            "  supervisor.name AS supervisor " +
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
