package lt.pavilonis.cmm.api.rest.presence;

import lt.pavilonis.cmm.common.util.TimeUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class PresenceTimeRepository {

   private static final Logger LOGGER = getLogger(PresenceTimeRepository.class.getSimpleName());
   private final NamedParameterJdbcTemplate jdbc;

   public PresenceTimeRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<PresenceTime> load(String cardCode, LocalDate periodStart, LocalDate periodEnd) {

      var opStart = LocalDateTime.now();
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("periodStart", periodStart);
      args.put("periodEnd", periodEnd);

      List<PresenceTime> result = jdbc.query("" +
                  "SELECT " +
                  "   CAST([dateTime] AS DATE)       AS workDay, " +
                  "   CAST(min([DATETIME] ) AS TIME) AS workDayStart, " +
                  "   CAST(max([DATETIME] ) AS TIME) AS workDayEnd, " +
                  "   ROUND( " +
                  "         ABS(DATEDIFF(SECOND, max([DATETIME]), min([DATETIME])) / 3600.0), " +
                  "         1 " +
                  "   )                               AS hourDiff " +
                  "FROM ScanLog " +
                  "WHERE cardCode = :cardCode " +
                  "  AND (:periodStart IS NULL OR :periodStart <= CAST([dateTime] AS DATE))" +
                  "  AND (:periodEnd IS NULL OR :periodEnd >= CAST([dateTime] AS DATE)) " +
                  "GROUP BY CAST([DATETIME] AS DATE) " +
                  "ORDER BY CAST([DATETIME] AS DATE) DESC",

            args,

            (rs, i) -> new PresenceTime(
                  rs.getTimestamp(1).toLocalDateTime().toLocalDate(),
                  rs.getTime(2).toLocalTime(),
                  rs.getTime(3).toLocalTime(),
                  rs.getDouble(4)
            )
      );
      LOGGER.info("Loaded work time entries [number={}, cardCode={}, t={}]",
            result.size(), cardCode, TimeUtils.duration(opStart));
      return result;
   }
}
