package lt.pavilonis.monpikas.key;

import lt.pavilonis.monpikas.scanlog.Scanner;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lt.pavilonis.monpikas.user.UserRepository.USER_MAPPER;
import static lt.pavilonis.monpikas.common.util.TimeUtils.duration;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Repository
public class KeyRepository {

   private static final Logger LOGGER = LoggerFactory.getLogger(KeyRepository.class);
   private final NamedParameterJdbcTemplate jdbc;

   public KeyRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   Key assign(long scannerId, long userId, int keyNumber) {
      var keyHolder = new GeneratedKeyHolder();
      var params =
            Map.of("scannerId", scannerId, "userId", userId, "keyNumber", keyNumber);

      var sql = "INSERT INTO KeyLog (scanner_id, user_id, keyNumber, assigned) " +
            "VALUES (:scannerId,  :userId, :keyNumber, TRUE)";

      jdbc.update(sql, new MapSqlParameterSource(params), keyHolder);
      return loadSingleKey(keyHolder.getKey().longValue());
   }

   Key unAssign(long scannerId, int keyNumber) {
      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO KeyLog (scanner_id, user_id, keyNumber, assigned)" +
            "     SELECT :scannerId, user_id, :keyNumber, FALSE " +
            "     FROM KeyLog " +
            "     WHERE scanner_id = :scannerId AND keyNumber = :keyNumber AND assigned " +
            "     ORDER BY dateTime DESC" +
            "     LIMIT 1";

      jdbc.update(sql, new MapSqlParameterSource(Map.of("scannerId", scannerId, "keyNumber", keyNumber)), keyHolder);
      return loadSingleKey(keyHolder.getKey().longValue());
   }

   private Key loadSingleKey(long keyLogId) {
      var sql = "SELECT " +
            "  kl.keyNumber, " +
            "  kl.dateTime, " +
            "  s.id AS scannerId," +
            "  s.name AS scannerName, " +
            "  kl.assigned, " +
            "  u.id, " +
            "  u.cardCode, " +
            "  u.name, " +
            "  u.birthDate, " +
            "  u.organizationGroup, " +
            "  u.organizationRole, " +
            "  supervisor.id AS supervisorId, " +
            "  supervisor.name AS supervisorName, " +
            "  NULL AS photo " +
            "FROM KeyLog kl " +
            "  JOIN Scanner s ON s.id = kl.scanner_id " +
            "  JOIN User u ON u.id = kl.user_id " +
            "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +
            "WHERE " +
            "  kl.id = :id";
      return jdbc.queryForObject(sql, Map.of("id", keyLogId), (rs, i) -> new Key(
            rs.getInt("keyNumber"),
            rs.getTimestamp("dateTime").toLocalDateTime(),
            USER_MAPPER.mapRow(rs),
            mapScanner(rs),
            mapAction(rs)
      ));
   }

   public List<Key> loadActive(Long scannerId, Long userId, String cardCode, Integer keyNumber) {
      var opStart = LocalDateTime.now();
      var args = new HashMap<String, Object>();
      args.put("userId", userId);
      args.put("cardCode", cardCode);
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);

      var query = "SELECT \n" +
            "     k.keyNumber, \n" +
            "     k.dateTime AS lastTimeTaken, \n" +
            "     s.id AS scannerId, \n" +
            "     s.name AS scannerName, \n" +
            "     u.id, \n" +
            "     u.cardCode, \n" +
            "     u.name, \n" +
            "     u.birthDate, \n" +
            "     u.organizationGroup, \n" +
            "     u.organizationRole, " +
            "     NULL AS photo, " +
            "     supervisor.id AS supervisorId, " +
            "     supervisor.name AS supervisorName " +

            "FROM KeyLog k \n" +
            "     JOIN Scanner s ON s.id = k.scanner_id \n" +

            "     JOIN ( \n" +
            "         SELECT keyNumber, MAX(dateTime) AS lastOperationMoment \n" +
            "         FROM KeyLog \n" +
            whereSection(userId, cardCode, keyNumber, scannerId) +
            "         GROUP BY keyNumber \n" +
            "     ) AS lastState ON lastState.keyNumber = k.keyNumber \n" +
            "         AND lastState.lastOperationMoment = k.dateTime \n" +

            "     JOIN User u ON u.id = k.user_id " +
            "     LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +

            "WHERE k.assigned";

      List<Key> result = jdbc.query(query, args, (rs, i) -> new Key(
            rs.getInt("keyNumber"),
            rs.getTimestamp("lastTimeTaken").toLocalDateTime(),
            USER_MAPPER.mapRow(rs),
            mapScanner(rs),
            KeyAction.ASSIGNED
      ));

      LOGGER.info("Loaded assigned keys [number={}, userId={}, cardCode={}, t={}]",
            result.size(), userId, cardCode, duration(opStart));
      return result;
   }

   private String whereSection(Long userId, String cardCode, Integer keyNumber, Long scannerId) {
      String result = scannerId == null ? EMPTY : ("scanner_id = " + scannerId);
      result = addCheck("user_id", userId, result);
      result = addCheck("cardCode", cardCode, result);
      result = addCheck("keyNumber", keyNumber, result);
      return result.isEmpty() ? "\n" : "\nWHERE " + result;
   }

   private String addCheck(String columnName, Object value, String result) {
      if (value == null) {
         return result;
      }

      String condition = columnName + " = " + (value instanceof String ? ("'" + value + "'") : value);
      return result.isEmpty()
            ? condition
            : result + ("\n  AND " + condition);
   }

   public List<Key> loadLog(LocalDate periodStart, LocalDate periodEnd, Long scannerId,
                            Integer keyNumber, KeyAction keyAction, String nameLike) {

      var opStart = LocalDateTime.now();
      var args = new HashMap<String, Object>();
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);
      args.put("periodStart", periodStart == null ? null : periodStart.atStartOfDay());
      args.put("periodEnd", periodEnd == null ? null : periodEnd.atTime(LocalTime.MAX));
      args.put("keyAction", keyAction == null ? null : keyAction == KeyAction.ASSIGNED);
      args.put("nameLike", QueryUtils.likeArg(nameLike));

      var sql = "SELECT " +
            "  k.keyNumber," +
            "  k.dateTime," +
            "  k.assigned, " +
            "  u.id, " +
            "  u.cardCode, " +
            "  u.name, " +
            "  u.birthDate, " +
            "  u.organizationRole, " +
            "  u.organizationGroup, " +
            "  NULL AS photo, " +
            "  supervisor.id AS supervisorId, " +
            "  supervisor.name AS supervisorName, " +
            "  s.id AS scannerId, " +
            "  s.name AS scannerName " +
            "FROM KeyLog k " +
            "  JOIN User u ON u.id = k.user_id " +
            "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id " +
            "  JOIN Scanner s ON s.id = k.scanner_id " +
            "WHERE " +
            "  k.dateTime >= :periodStart " +
            "  AND (:periodEnd IS NULL OR k.dateTime <= :periodEnd) " +
            "  AND (:scannerId IS NULL OR k.scanner_id = :scannerId) " +
            "  AND (:keyNumber IS NULL OR k.keyNumber = :keyNumber) " +
            "  AND (:keyAction IS NULL OR k.assigned = :keyAction) " +
            "  AND (:nameLike IS NULL OR u.name LIKE :nameLike) " +
            "ORDER BY k.dateTime DESC";

      List<Key> result = jdbc.query(sql, args, (rs, i) -> new Key(
            rs.getInt("keyNumber"),
            rs.getTimestamp("dateTime").toLocalDateTime(),
            USER_MAPPER.mapRow(rs),
            mapScanner(rs),
            mapAction(rs)
      ));

      LOGGER.info(
            "Loaded log [periodStart={}, periodEnd={}, scannerId={}, key={}, action={}, name={}, size={}, t={}]",
            periodStart == null ? EMPTY : DateTimeFormatter.ISO_LOCAL_DATE.format(periodStart),
            periodEnd == null ? EMPTY : DateTimeFormatter.ISO_LOCAL_DATE.format(periodEnd),
            scannerId == null ? EMPTY : scannerId,
            keyNumber == null ? EMPTY : keyNumber,
            keyAction == null ? EMPTY : keyAction.name(),
            StringUtils.stripToEmpty(nameLike),
            result.size(),
            duration(opStart)
      );
      return result;
   }

   private Scanner mapScanner(ResultSet rs) throws SQLException {
      return new Scanner(rs.getLong("scannerId"), rs.getString("scannerName"));
   }

   private KeyAction mapAction(ResultSet rs) throws SQLException {
      return rs.getBoolean("assigned") ? KeyAction.ASSIGNED : KeyAction.UNASSIGNED;
   }
}
