package lt.pavilonis.cmm.school.classroom;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomOccupancy;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class OccupancyDurationCheckJob {

   private static final Logger LOGGER = LoggerFactory.getLogger(OccupancyDurationCheckJob.class.getSimpleName());
   private final int durationLimitMinutes;
   private final ClassroomRepository repository;
   private final NamedParameterJdbcTemplate jdbcSalto;

   public OccupancyDurationCheckJob(@Value("${classroom.occupancyDuration.limitMinutes}") int durationLimitMinutes,
                                    ClassroomRepository repository, NamedParameterJdbcTemplate jdbcSalto) {
      this.durationLimitMinutes = durationLimitMinutes;
      this.repository = repository;
      this.jdbcSalto = jdbcSalto;
   }

   @Scheduled(fixedRateString = "${classroom.occupancyDuration.checkIntervalMillis}")
   public void freeClassrooms() {

      List<ClassroomOccupancy> active = repository.loadActive(Collections.emptyList(), null);

      LocalDateTime earliestOccupancyEventDateAllowed = LocalDateTime.now()
            .minusMinutes(durationLimitMinutes);

      List<ClassroomOccupancy> classroomsToFree = active.stream()
            .filter(ClassroomOccupancy::isOccupied)
            .filter(co -> co.getDateTime().isBefore(earliestOccupancyEventDateAllowed))
            .collect(toList());

      if (classroomsToFree.isEmpty()) {
         LOGGER.info("No classrooms exceeding occupancy limit found [checked={}]", active.size());
         return;
      }

      @SuppressWarnings("unchecked")
      Map<String, ?>[] args = classroomsToFree.stream()
            .map(room -> ImmutableMap.of(
                  "building", room.getBuilding(),
                  "number", room.getClassroomNumber()
            ))
            .toArray(Map[]::new);

      jdbcSalto.batchUpdate(
            "INSERT INTO mm_ClassroomOccupancy (building, classroomNumber, occupied) " +
                  "VALUES (:building, :number, '0')",
            args
      );
      LOGGER.info("Marked classrooms exceeding occupancy limit as free [classrooms={}]",
            classroomsToFree.stream()
                  .map(room -> room.getBuilding() + "-" + room.getClassroomNumber())
                  .collect(joining(","))
      );
   }
}
