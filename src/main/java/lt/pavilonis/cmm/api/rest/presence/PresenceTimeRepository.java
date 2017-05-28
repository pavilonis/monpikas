package lt.pavilonis.cmm.api.rest.presence;

import lt.pavilonis.util.TimeUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class PresenceTimeRepository {

   private static final Logger LOG = getLogger(PresenceTimeRepository.class.getSimpleName());

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   public List<PresenceTime> load(String cardCode) {
      LocalDateTime opStart = LocalDateTime.now();

      List<PresenceTime> result = jdbcSalto.query("" +
                  "SELECT " +
                  "   CAST([dateTime] AS DATE)       AS workDay, " +
                  "   CAST(min([DATETIME] ) AS TIME) AS workDayStart, " +
                  "   CAST(max([DATETIME] ) AS TIME) AS workDayEnd, " +
                  "   ROUND( " +
                  "         ABS(DATEDIFF(SECOND, max([DATETIME]), min([DATETIME])) / 3600.0), " +
                  "         1 " +
                  "   )                              AS hourDiff " +
                  "FROM mm_ScanLog " +
                  "WHERE cardCode = :cardCode " +
                  "GROUP BY CAST([DATETIME] AS DATE) " +
                  "ORDER BY CAST([DATETIME] AS DATE) DESC",
            Collections.singletonMap("cardCode", cardCode),
            (rs, i) -> new PresenceTime(
                  rs.getTimestamp(1).toLocalDateTime().toLocalDate(),
                  rs.getTime(2).toLocalTime(),
                  rs.getTime(3).toLocalTime(),
                  rs.getDouble(4)
            )
      );
      LOG.info("Loaded work time entries [number={}, cardCode={}, duration={}]",
            result.size(), cardCode, TimeUtils.duration(opStart));
      return result;
   }
}
