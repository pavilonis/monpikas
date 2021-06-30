package lt.pavilonis.cmm.api.rest.key;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.common.util.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lt.pavilonis.cmm.common.util.TimeUtils.duration;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class KeyRepository {

   private static final Logger LOGGER = getLogger(KeyRepository.class.getSimpleName());
   private static final BiMap<KeyAction, Integer> KEY_ACTION_INTEGER_MAP = HashBiMap.create(ImmutableMap.of(
         KeyAction.ASSIGNED, 1,
         KeyAction.UNASSIGNED, 0
   ));
   private final NamedParameterJdbcTemplate jdbc;
   private final UserRepository userRepository;

   public KeyRepository(NamedParameterJdbcTemplate jdbc, UserRepository userRepository) {
      this.jdbc = jdbc;
      this.userRepository = userRepository;
   }

   Key assign(long scannerId, String cardCode, int keyNumber) {
      var keyHolder = new GeneratedKeyHolder();
      var params =
            Map.of("scannerId", scannerId, "cardCode", cardCode, "keyNumber", keyNumber);

      var sql = "INSERT INTO KeyLog (scanner_id, cardCode, keyNumber, assigned) " +
            "VALUES (:scannerId,  :cardCode, :keyNumber, TRUE)";

      jdbc.update(sql, new MapSqlParameterSource(params), keyHolder);
      return loadSingleKey(keyHolder.getKey().longValue());
   }

   Key unAssign(long scannerId, int keyNumber) {
      var keyHolder = new GeneratedKeyHolder();
      var sql = "INSERT INTO KeyLog (scanner_id, cardCode, keyNumber, assigned)" +
            "     SELECT :scannerId, cardCode, :keyNumber, FALSE " +
            "     FROM KeyLog " +
            "     WHERE scanner_id = :scannerId AND keyNumber = :keyNumber AND assigned " +
            "     ORDER BY dateTime DESC" +
            "     LIMIT 1";

      jdbc.update(sql, new MapSqlParameterSource(Map.of("scannerId", scannerId, "keyNumber", keyNumber)), keyHolder);
      return loadSingleKey(keyHolder.getKey().longValue());
   }

   private Key loadSingleKey(long id) {
      var sql = "SELECT " +
            "  kl.keyNumber, " +
            "  kl.dateTime, " +
            "  kl.cardCode, " +
            "  s.id," +
            "  s.name, " +
            "  kl.assigned " +
            "FROM KeyLog kl " +
            "  JOIN Scanner s ON s.id = kl.scanner_id " +
            "WHERE " +
            "  kl.id = :id";
      return jdbc.queryForObject(sql, Map.of("id", id), (rs, i) -> new Key(
            rs.getInt(1),
            rs.getTimestamp(2).toLocalDateTime(),
            userRepository.load(rs.getString(3), false),
            new Scanner(rs.getLong(4), rs.getString(5)),
            KEY_ACTION_INTEGER_MAP.inverse().get(rs.getInt(6))
      ));
   }

   boolean isAvailable(long scannerId, int keyNumber) {
      List<Key> result = loadActive(scannerId, null, keyNumber);
      return result.isEmpty();
   }

   public List<Key> loadActive(Long scannerId, String cardCode, Integer keyNumber) {
      var opStart = LocalDateTime.now();
      var args = new HashMap<String, Object>();
      args.put("cardCode", cardCode);
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);

      var query = "SELECT \n" +
            "     k.keyNumber, \n" +
            "     k.dateTime AS lastTimeTaken, \n" +
            "     s.id AS scannerId, \n" +
            "     s.name AS scannerName, \n" +
            "     u.cardCode, \n" +
            "     u.name, \n" +
            "     u.birthDate, \n" +
            "     u.organizationGroup, \n" +
            "     u.organizationRole \n" +

            "FROM KeyLog k \n" +
            "     JOIN Scanner s ON s.id = k.scanner_id \n" +

            "     JOIN ( \n" +
            "         SELECT keyNumber, MAX(dateTime) AS lastOperationMoment \n" +
            "         FROM KeyLog \n" +
            whereSection(cardCode, keyNumber, scannerId) +
            "         GROUP BY keyNumber \n" +
            "     ) AS lastState ON lastState.keyNumber = k.keyNumber \n" +
            "         AND lastState.lastOperationMoment = k.dateTime \n" +

            "     JOIN User u ON u.cardCode = k.cardCode \n" +

            "WHERE k.assigned \n" +
            "  AND u.cardCode IS NOT NULL";

      List<Key> result = jdbc.query(query, args, (rs, i) -> new Key(
            rs.getInt("keyNumber"),
            rs.getTimestamp("lastTimeTaken").toLocalDateTime(),
            new User(
                  rs.getString("cardCode"),
                  rs.getString("name"),
                  rs.getString("organizationGroup"),
                  rs.getString("organizationRole"),
                  null,
                  rs.getString("birthDate")
            ),
            new Scanner(rs.getLong("scannerId"), rs.getString("scannerName")),
            KeyAction.ASSIGNED
      ));

      LOGGER.info("Loaded assigned keys [number={}, cardCode={}, t={}]", result.size(), cardCode, duration(opStart));
      return result;
   }

   private String whereSection(String cardCode, Integer keyNumber, Long scannerId) {

      String result = scannerId == null ? EMPTY : ("scanner_id = " + scannerId);

      if (cardCode != null) {
         String check = "cardCode = '" + cardCode + "'";
         if (result.isEmpty()) {
            result = check;
         } else {
            result += ("\n  AND " + check);
         }
      }

      if (keyNumber != null) {
         String check = "keyNumber = " + keyNumber;
         if (result.isEmpty()) {
            result = check;
         } else {
            result += ("\n  AND " + check);
         }
      }

      return result.isEmpty() ? "\n" : "\nWHERE " + result;
   }

   public List<Key> loadLog(LocalDate periodStart, LocalDate periodEnd, Long scannerId,
                            Integer keyNumber, KeyAction keyAction, String nameLike) {

      var opStart = LocalDateTime.now();
      var args = new HashMap<String, Object>();
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);
      args.put("periodStart", periodStart == null ? null : periodStart.atStartOfDay());
      args.put("periodEnd", periodEnd == null ? null : periodEnd.atTime(LocalTime.MAX));
      args.put("keyAction", keyAction == null ? null : KEY_ACTION_INTEGER_MAP.get(keyAction));
      args.put("nameLike", QueryUtils.likeArg(nameLike));

      var sql = "SELECT " +
            "  k.keyNumber," +
            "  k.dateTime," +
            "  k.assigned, " +
            "  u.cardCode, " +
            "  u.name, " +
            "  u.birthDate, " +
            "  u.organizationRole, " +
            "  u.organizationGroup," +
            "  s.id AS scannerId, " +
            "  s.name AS scannerName " +
            "FROM KeyLog k " +
            "  JOIN User u ON u.cardCode = k.cardCode " +
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
            new User(
                  rs.getString("cardCode"),
                  rs.getString("name"),
                  rs.getString("organizationGroup"),
                  rs.getString("organizationRole"),
                  null,
                  rs.getString("birthDate")
            ),
            new Scanner(rs.getLong("scannerId"), rs.getString("scannerName")),
            KEY_ACTION_INTEGER_MAP.inverse().get(rs.getInt("assigned"))
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
}
