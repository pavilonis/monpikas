package lt.pavilonis.cmm.school.scanlog;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScanLogCleanupJob {

   private static final Logger LOGGER = LoggerFactory.getLogger(ScanLogCleanupJob.class.getSimpleName());
   private final int historyDaysThreshold;
   private final NamedParameterJdbcTemplate jdbcSalto;

   public ScanLogCleanupJob(@Value("${scanlog.history.cleanup.days:300}") int historyDaysThreshold,
                            NamedParameterJdbcTemplate jdbcSalto) {
      this.historyDaysThreshold = historyDaysThreshold;
      this.jdbcSalto = jdbcSalto;
   }

   @Scheduled(cron = "${scanlog.history.cleanup.cron:0 50 23 * * SAT}")
   public void deleteOldRecords() {

      LocalDateTime opStart = LocalDateTime.now();
      LocalDateTime threshold = opStart.minusDays(historyDaysThreshold);

      int recordsDeleted = jdbcSalto.update(
            "DELETE FROM mm_ScanLog WHERE dateTime < :threshold",
            ImmutableMap.of("threshold", threshold)
      );
      LOGGER.info("Deleted old mm_ScanLog records [number={}, t={}]",
            recordsDeleted, TimeUtils.duration(opStart));
   }
}
