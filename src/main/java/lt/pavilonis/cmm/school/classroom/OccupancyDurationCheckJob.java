package lt.pavilonis.cmm.school.classroom;

import lt.pavilonis.cmm.api.rest.classroom.ClassroomOccupancy;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OccupancyDurationCheckJob {

   private static final Logger LOG = LoggerFactory.getLogger(
         OccupancyDurationCheckJob.class.getSimpleName());

   @Value("${classroom.occupancyDuration.limitMinutes}")
   private int durationLimitMinutes;

   @Autowired
   private ClassroomRepository repository;

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Scheduled(fixedRateString = "${classroom.occupancyDuration.checkIntervalMillis}")
   public void freeClassrooms() {

      List<ClassroomOccupancy> active = repository.loadActive();

      LocalDateTime earliestOccupancyEventDateAllowed = LocalDateTime.now()
            .minusMinutes(durationLimitMinutes);

      Set<Integer> classroomNumbersToFree = active.stream()
            .filter(ClassroomOccupancy::isOccupied)
            .filter(co -> co.getDateTime().isBefore(earliestOccupancyEventDateAllowed))
            .map(ClassroomOccupancy::getClassroomNumber)
            .collect(Collectors.toSet());

      if (classroomNumbersToFree.isEmpty()) {
         LOG.info("No classrooms exceeding occupancy limit found [checked={}]", active.size());
         return;
      }

      @SuppressWarnings("unchecked")
      Map<String, ?>[] args = classroomNumbersToFree.stream()
            .map(number -> Collections.singletonMap("number", number))
            .toArray(Map[]::new);

      jdbcSalto.batchUpdate(
            "INSERT INTO mm_ClassroomOccupancy (classroomNumber, occupied) VALUES (:number, '0')",
            args
      );
      LOG.info("Mark classrooms exceeding occupancy limit as free [numbers={}]",
            classroomNumbersToFree);
   }
}
