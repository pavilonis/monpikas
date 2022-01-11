package lt.pavilonis.monpikas.scanlog;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import lt.pavilonis.monpikas.common.util.TimeUtils;
import lt.pavilonis.monpikas.key.Key;
import lt.pavilonis.monpikas.key.KeyRepository;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBrief;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBriefFilter;
import lt.pavilonis.monpikas.scanlog.brief.ScanLogBriefMapper;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.user.UserRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;
import static lt.pavilonis.monpikas.user.UserRepository.USER_MAPPER;

@Slf4j
@Repository
public class ScanLogRepository {

   private static final int DEFAULT_NUMBER_OF_SCANLOG_ELEMENTS_TO_LOAD = 20;
   private static final String FROM_WHERE_BLOCK =
         "FROM ScanLog sl \n" +
               "  JOIN Scanner sc ON sc.id = sl.scanner_id \n" +
               "  JOIN User u ON u.id = sl.user_id \n" +
               "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id \n" +
               "WHERE sl.dateTime >= :periodStart \n" +
               "  AND (:periodEnd IS NULL OR DATE(sl.dateTime) <= :periodEnd) \n" +
               "  AND (:scannerId IS NULL OR sc.id = :scannerId) \n" +
               "  AND (:role IS NULL OR u.organizationRole = :role) \n" +
               "  AND (:text IS NULL OR u.cardCode LIKE :text OR u.name LIKE :text) \n";

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
      List<ScanLog> result = load(scannerId, scanLogId);
      if (result.size() != 1) {
         throw new RuntimeException("Not expected number of loaded entries: " + result.size());
      }
      return result.get(0);
   }

   public List<ScanLog> readLastScanLogs(long scannerId) {
      return load(scannerId, null);
   }

   private List<ScanLog> load(long scannerId, Long scanLogId) {
      var params = new HashMap<String, Object>();
      params.put("scanLogId", scanLogId);
      params.put("scannerId", scannerId);
      params.put("limit", DEFAULT_NUMBER_OF_SCANLOG_ELEMENTS_TO_LOAD);

      return jdbc.query(
            "SELECT " +
                  "  sl.dateTime, " +
                  "  u.id, " +
                  "  u.created, " +
                  "  u.updated, " +
                  "  u.cardCode, " +
                  "  u.name, " +
                  "  u.birthDate, " +
                  "  u.organizationGroup, " +
                  "  u.organizationRole, " +
                  "  u.photo, " +
                  "  supervisor.id AS supervisorId, " +
                  "  supervisor.name AS supervisor " +
                  "FROM ScanLog sl " +
                  "  JOIN User u ON u.id = sl.user_id " +
                  "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +
                  "WHERE sl.scanner_id = :scannerId " +
                  "  AND (:scanLogId IS NULL OR sl.id = :scanLogId)" +
                  "LIMIT :limit",
            params,
            (rs, i) -> {
               User user = USER_MAPPER.mapRow(rs);
               long userId = rs.getLong("id");
               List<Key> keys = keyRepository.loadActive(scannerId, userId, null, null);
               return new ScanLog(QueryUtils.getLocalDateTime(rs, "dateTime"), user, keys);
            }
      );
   }

   private Optional<Long> save(long scannerId, String cardCode) {

      User user = userRepository.load(cardCode, false);
      if (user == null) {
         log.warn("Skipping scan log: user not found [scannerId={}, cardCode={}]", scannerId, cardCode);
         return Optional.empty();
      }

      var args = Map.of("userId", user.getId(), "scannerId", scannerId);
      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO ScanLog (user_id, scanner_id) VALUES (:userId, :scannerId)";

      jdbc.update(sql, new MapSqlParameterSource(args), keyHolder);
      log.info("ScanLog saved");
      return Optional.of(keyHolder.getKey().longValue());
   }

   public int loadBriefSize(ScanLogBriefFilter filter) {
      return jdbc.queryForObject("SELECT COUNT(*) " + FROM_WHERE_BLOCK, commonArgs(filter), Integer.class);
   }

   public List<ScanLogBrief> loadBrief(ScanLogBriefFilter filter, List<QuerySortOrder> sortOrders,
                                       long offset, long limit) {
      Map<String, Object> args = commonArgs(filter);
      args.put("argOffset", offset);
      args.put("argLimit", limit);

      var sql = "SELECT \n" +
            "  sl.dateTime as dateTime, \n" +
            "  sc.name AS scanner, \n" +
            "  u.name AS name, \n" +
            "  u.cardCode AS cardCode, \n" +
            "  u.organizationGroup AS `group`, \n" +
            "  u.organizationRole AS role, \n" +
            "  supervisor.name AS supervisor \n" +
            FROM_WHERE_BLOCK +
            createOrder(sortOrders) + "\n" +
            "LIMIT :argLimit OFFSET :argOffset";

      return jdbc.query(sql, args, new ScanLogBriefMapper());
   }

   private String createOrder(List<QuerySortOrder> sortOrders) {
      if (sortOrders.isEmpty()) {
         return "ORDER BY dateTime DESC";
      }
      QuerySortOrder order = sortOrders.get(0);
      return "ORDER BY `" + order.getSorted() + "`" +
            (order.getDirection() == SortDirection.ASCENDING ? " ASC" : " DESC");
   }

   private Map<String, Object> commonArgs(ScanLogBriefFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());
      args.put("scannerId", filter.getScannerId());
      args.put("role", StringUtils.hasText(filter.getRole()) ? filter.getRole().strip() : null);
      args.put("text", QueryUtils.likeArg(filter.getText()));
      return args;
   }

   public List<ScanLogBrief> loadLastUserLocations(String text) {
      var start = now();
      Map<String, Object> args = new HashMap<>();
      args.put("text", QueryUtils.likeArg(text));

      List<ScanLogBrief> result = jdbc.query(
            "SELECT " +
                  "  sl.dateTime, " +
                  "  u.cardCode, " +
                  "  u.name, " +
                  "  u.organizationGroup AS `group`, " +
                  "  u.organizationRole AS role, " +
                  "  sv.name AS supervisor, " +
                  "  s.name AS scanner " +
                  "FROM ScanLog sl " +
                  "  JOIN User u ON u.id = sl.user_id " +
                  "  LEFT JOIN User sv ON sv.id = u.supervisor_id " +
                  "  JOIN Scanner s ON s.id = sl.scanner_id " +
                  "WHERE sl.dateTime > CURRENT_DATE() " +
                  "  AND (:text IS NULL OR u.name LIKE :text OR s.name LIKE :text)",
            args,
            new ScanLogBriefMapper()
      );

      List<ScanLogBrief> filteredResult = result
            .stream()
            .collect(Collectors.groupingBy(ScanLogBrief::getName))
            .values()
            .stream()
            .flatMap(this::composeUserLogs)
            .collect(Collectors.toList());

      log.info("Loaded last user locations [text={}, number={}, filtered={}, t={}]",
            text, result.size(), filteredResult.size(), TimeUtils.duration(start));

      return filteredResult;
   }

   private Stream<ScanLogBrief> composeUserLogs(List<ScanLogBrief> groupedByName) {
      return groupedByName
            .stream()
            .collect(Collectors.groupingBy(ScanLogBrief::getScanner))
            .values()
            .stream()
            // Taking single latest entry for location user was in
            .map(groupedByLocation -> groupedByLocation
                  .stream()
                  .max(Comparator.comparing(ScanLogBrief::getDateTime))
                  .orElseThrow(RuntimeException::new)
            )
            .sorted(Comparator.comparing(ScanLogBrief::getDateTime).reversed())
            .limit(3);
   }
}
