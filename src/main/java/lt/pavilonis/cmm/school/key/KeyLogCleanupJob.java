package lt.pavilonis.cmm.school.key;

import lt.pavilonis.cmm.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class KeyLogCleanupJob {

   private static final Logger LOGGER = LoggerFactory.getLogger(KeyLogCleanupJob.class.getSimpleName());
   private final int historyDaysThreshold;
   private final NamedParameterJdbcTemplate jdbc;

   public KeyLogCleanupJob(@Value("${keylog.history.cleanup.days:300}") int historyDaysThreshold,
                           NamedParameterJdbcTemplate jdbc) {
      this.historyDaysThreshold = historyDaysThreshold;
      this.jdbc = jdbc;
   }

   @Scheduled(cron = "${keylog.history.cleanup.cron:0 45 23 * * SAT}")
   public void deleteOldRecords() {

      var opStart = LocalDateTime.now();
      var threshold = opStart.minusDays(historyDaysThreshold);
      var sql = "DELETE FROM KeyLog WHERE dateTime < :threshold";

      int recordsDeleted = jdbc.update(sql, Map.of("threshold", threshold));
      LOGGER.info("Deleted old KeyLog records [number={}, t={}]",
            recordsDeleted, TimeUtils.duration(opStart));
   }
}
