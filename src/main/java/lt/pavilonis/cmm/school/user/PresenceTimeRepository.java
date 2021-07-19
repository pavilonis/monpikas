package lt.pavilonis.cmm.school.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static java.time.LocalDateTime.now;
import static lt.pavilonis.cmm.common.util.TimeUtils.duration;

@Repository
public class PresenceTimeRepository {

   private static final Logger LOGGER = LoggerFactory.getLogger(PresenceTimeRepository.class);
   private final NamedParameterJdbcTemplate jdbc;

   public PresenceTimeRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<PresenceTime> load(Long userId, LocalDate periodStart, LocalDate periodEnd) {
      if (userId == null) {
         return List.of();
      }
      var opStart = now();
      var params = new HashMap<String, Object>();
      params.put("userId", userId);
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
            "WHERE user_id = :userId " +
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

      LOGGER.info("Loaded work time entries [number={}, userId={}, t={}]", result.size(), userId, duration(opStart));
      return result;
   }
}
