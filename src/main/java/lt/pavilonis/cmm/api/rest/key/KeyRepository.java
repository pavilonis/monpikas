package lt.pavilonis.cmm.api.rest.key;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class KeyRepository {

   private static final Logger LOG = getLogger(KeyRepository.class.getSimpleName());
   private static final BiMap<KeyAction, Integer> KEY_ACTION_INTEGER_MAP = HashBiMap.create(ImmutableMap.of(
         KeyAction.ASSIGNED, 1,
         KeyAction.UNASSIGNED, 0
   ));

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Autowired
   private UserRepository userRepository;

   Key assign(long scannerId, String cardCode, int keyNumber) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcSalto.update("" +
                  "INSERT INTO mm_KeyLog (scanner_id, cardCode, keyNumber, assigned) " +
                  "VALUES (:scannerId,  :cardCode, :keyNumber, :keyAction)",
            new MapSqlParameterSource(ImmutableMap.of(
                  "scannerId", scannerId,
                  "cardCode", cardCode,
                  "keyNumber", keyNumber,
                  "keyAction", KEY_ACTION_INTEGER_MAP.get(KeyAction.ASSIGNED)
            )),
            keyHolder
      );
      return loadSingleKey(keyHolder.getKey().longValue());
   }

   Key unAssign(long scannerId, int keyNumber) {

      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcSalto.update("" +
                  "INSERT INTO mm_KeyLog (scanner_id, cardCode, keyNumber, assigned) VALUES (" +
                  "  :scannerId," +
                  "  (" +
                  "     SELECT TOP 1 cardCode " +
                  "     FROM mm_KeyLog " +
                  "     WHERE scanner_id = :scannerId AND keyNumber = :keyNumber " +
                  "     ORDER BY DATETIME DESC" +
                  "  )," +
                  "  :keyNumber, " +
                  "  :keyAction" +
                  ")",
            new MapSqlParameterSource(ImmutableMap.of(
                  "scannerId", scannerId,
                  "keyNumber", keyNumber,
                  "keyAction", KEY_ACTION_INTEGER_MAP.get(KeyAction.UNASSIGNED)
            )),
            keyHolder
      );

      return loadSingleKey(keyHolder.getKey().longValue());
   }

   private Key loadSingleKey(long id) {
      return jdbcSalto.queryForObject("" +
                  "SELECT " +
                  "  kl.keyNumber, " +
                  "  kl.dateTime, " +
                  "  kl.cardCode, " +
                  "  scan.id," +
                  "  scan.name, " +
                  "  kl.assigned " +
                  "FROM mm_KeyLog kl " +
                  "  JOIN mm_Scanner scan ON scan.id = kl.scanner_id " +
                  "WHERE " +
                  "  kl.id = :id",
            Collections.singletonMap("id", id),
            (rs, i) -> new Key(
                  rs.getInt(1),
                  rs.getTimestamp(2).toLocalDateTime(),
                  userRepository.load(rs.getString(3), false),
                  new Scanner(
                        rs.getLong(4),
                        rs.getString(5)
                  ),
                  KEY_ACTION_INTEGER_MAP.inverse().get(rs.getInt(6))
            )
      );
   }

   boolean isAvailable(long scannerId, int keyNumber) {
      List<Key> result = loadActive(scannerId, null, keyNumber);
      return result.isEmpty();
   }

   public List<Key> loadActive(Long scannerId, String cardCode, Integer keyNumber) {
      LocalDateTime opStart = LocalDateTime.now();

      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);

      List<Key> result = jdbcSalto.query("" +
                  "SELECT " +
                  "     kl.keyNumber, " +
                  "     kl.dateTime AS lastTimeTaken, " +
                  "     s.id AS scannerId, " +
                  "     s.name AS scannerName, " +

                  "     c.ROMCode AS cardCode, " +
                  "     u.FirstName AS firstName, " +
                  "     u.LastName AS lastName, " +
                  "     u.dummy2 AS birthDate, " +
                  "     u.dummy3 AS userGroup, " +
                  "     u.dummy4 AS userRole " +

                  "FROM CMM2.dbo.mm_KeyLog kl " +
                  "     JOIN CMM2.dbo.mm_Scanner s ON s.id = kl.scanner_id " +

                  "     JOIN ( " +
                  "         SELECT keyNumber, MAX(dateTime) AS lastOperationMoment " +
                  "         FROM CMM2.dbo.mm_KeyLog " +
                  whereSection(cardCode, keyNumber, scannerId) +
                  "         GROUP BY keyNumber " +
                  "     ) AS lastState ON lastState.keyNumber = kl.keyNumber " +
                  "         AND lastState.lastOperationMoment = kl.dateTime " +

                  "     JOIN tb_Cards c ON c.ROMCode = kl.cardCode " +
                  "     JOIN tb_Users u ON u.Cardcode = c.Cardcode " +

                  "WHERE kl.assigned = 1 " +
                  "  AND u.CardCode IS NOT NULL",
            args,
            (rs, i) -> new Key(
                  rs.getInt("keyNumber"),
                  rs.getTimestamp("lastTimeTaken").toLocalDateTime(),
                  new User(
                        rs.getString("cardCode"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("userGroup"),
                        rs.getString("userRole"),
                        null,
                        rs.getString("birthDate")
                  ),
                  new Scanner(rs.getLong("scannerId"), rs.getString("scannerName")),
                  KeyAction.ASSIGNED
            )
      );
      LOG.info("Loaded assigned keys [number={}, cardCode={}, duration={}]",
            result.size(), cardCode, TimeUtils.duration(opStart));
      return result;
   }

   private String whereSection(String cardCode, Integer keyNumber, Long scannerId) {

      String result = scannerId == null
            ? StringUtils.EMPTY
            : "scanner_id = :scannerId";

      if (cardCode != null) {
         String check = "cardCode = :cardCode";
         result = result.isEmpty() ? check : " AND " + check;
      }

      if (keyNumber != null) {
         String check = "keyNumber = :keyNumber";
         if (result.isEmpty()) {
            result = check;
         } else {
            result += (" AND " + check);
         }
      }

      return result.isEmpty() ? StringUtils.EMPTY : "WHERE " + result;
   }

   public List<Key> loadLog(LocalDate periodStart, LocalDate periodEnd, Long scannerId,
                            Integer keyNumber, KeyAction keyAction, String nameLike) {

      LocalDateTime opStart = LocalDateTime.now();

      Map<String, Object> args = new HashMap<>();
      args.put("scannerId", scannerId);
      args.put("keyNumber", keyNumber);
      args.put("periodStart", periodStart == null ? null : periodStart.atStartOfDay());
      args.put("periodEnd", periodEnd == null ? null : periodEnd.atTime(LocalTime.MAX));
      args.put("keyAction", keyAction == null ? null : KEY_ACTION_INTEGER_MAP.get(keyAction));
      args.put("nameLike", StringUtils.isBlank(nameLike) ? null : "%" + nameLike + "%");

      List<Key> result = jdbcSalto.query("" +
                  "SELECT " +
                  "  kl.keyNumber AS keyNumber," +
                  "  kl.dateTime AS dateTime," +
                  "  kl.assigned AS assigned, " +
                  "  usr.cardCode AS cardCode, " +
                  "  usr.FirstName AS firstName, " +
                  "  usr.LastName AS lastName, " +
                  "  usr.Dummy2 AS birthDate, " +
                  "  usr.Dummy3 AS userRole, " +
                  "  usr.Dummy4 AS userGroup," +
                  "  sc.id AS scannerId, " +
                  "  sc.name AS scannerName " +
                  "FROM mm_KeyLog kl " +
                  "  JOIN tb_Cards card ON card.ROMCode = kl.cardCode " +
                  "  JOIN tb_Users usr ON usr.Cardcode = card.Cardcode " +
                  "  JOIN mm_Scanner sc ON sc.id = kl.scanner_id " +
                  "WHERE " +
                  "  kl.dateTime >= :periodStart " +
                  "  AND (:periodEnd IS NULL OR kl.dateTime <= :periodEnd) " +
                  "  AND (:scannerId IS NULL OR kl.scanner_id = :scannerId) " +
                  "  AND (:keyNumber IS NULL OR kl.keyNumber = :keyNumber) " +
                  "  AND (:keyAction IS NULL OR kl.assigned = :keyAction) " +
                  "  AND (:nameLike IS NULL OR usr.FirstName LIKE :nameLike OR usr.LastName LIKE :nameLike) " +
                  "ORDER BY kl.dateTime DESC ",
            args,
            (rs, i) -> new Key(
                  rs.getInt("keyNumber"),
                  rs.getTimestamp("dateTime").toLocalDateTime(),
                  new User(
                        rs.getString("cardCode"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("userGroup"),
                        rs.getString("userRole"),
                        null,
                        rs.getString("birthDate")
                  ),
                  new Scanner(rs.getLong("scannerId"), rs.getString("scannerName")),
                  KEY_ACTION_INTEGER_MAP.inverse().get(rs.getInt("assigned"))
            ));
      LOG.info(
            "Loaded log [periodStart={}, periodEnd={}, scannerId={}," +
                  " keyNumber={}, keyAction={}, name={}, size={}, duration={}]",
            periodStart == null ? "" : DateTimeFormatter.ISO_LOCAL_DATE.format(periodStart),
            periodEnd == null ? "" : DateTimeFormatter.ISO_LOCAL_DATE.format(periodEnd),
            scannerId == null ? "" : scannerId,
            keyNumber == null ? "" : keyNumber,
            keyAction == null ? "" : keyAction.name(),
            StringUtils.stripToEmpty(nameLike),
            result.size(),
            TimeUtils.duration(opStart)
      );
      return result;
   }
}
