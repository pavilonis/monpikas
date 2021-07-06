package lt.pavilonis.cmm.api.rest.presence;

import lt.pavilonis.cmm.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Repository
public class PresenceTimeRepository {

   private final Logger logger = LoggerFactory.getLogger(getClass());
   private final NamedParameterJdbcTemplate jdbc;

   public PresenceTimeRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<PresenceTime> load(String cardCode, LocalDate periodStart, LocalDate periodEnd) {
      var opStart = LocalDateTime.now();
      var params = new HashMap<String, Object>();
      params.put("cardCode", cardCode);
      params.put("periodStart", periodStart);
      params.put("periodEnd", periodEnd);

      var sql = "SELECT " +
            "   DATE(dateTime)      AS workDay, " +
            "   MIN(TIME(dateTime)) AS workDayStart, " +
            "   MAX(TIME(dateTime)) AS workDayEnd, " +
            "   ROUND( " +
            "         ABS(" +
            "           TIME_TO_SEC(" +
            "              TIMEDIFF(MAX(dateTime), MIN(dateTime))" +
            "           ) / 3600.0" +
            "         ), 1 " +
            "   )                   AS hourDiff " +
            "FROM ScanLog " +
            "WHERE cardCode = :cardCode " +
            "  AND (:periodStart IS NULL OR :periodStart <= dateTime)" +
            "  AND (:periodEnd IS NULL OR :periodEnd >= dateTime) " +
            "GROUP BY workDay " +
            "ORDER BY workDay DESC";

      List<PresenceTime> result = jdbc.query(sql, params, (rs, i) -> new PresenceTime(
            rs.getDate(1).toLocalDate(),
            rs.getTime(2).toLocalTime(),
            rs.getTime(3).toLocalTime(),
            rs.getDouble(4)
      ));

      logger.info("Loaded work time entries [number={}, cardCode={}, t={}]",
            result.size(), cardCode, TimeUtils.duration(opStart));
      return result;
   }
}
