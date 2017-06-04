package lt.pavilonis.cmm.api.rest.classroom;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClassroomRepository {

   private static final Logger LOG = LoggerFactory.getLogger(ClassroomRepository.class.getSimpleName());

   private static final RowMapper<ClassroomOccupancy> ROW_MAPPER =
         (rs, i) -> new ClassroomOccupancy(
               rs.getTimestamp(1).toLocalDateTime(),
               rs.getBoolean(2),
               rs.getInt(3)
         );

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   public List<ClassroomOccupancy> loadActive() {

      LocalDateTime opStart = LocalDateTime.now();

      List<ClassroomOccupancy> result = jdbcSalto.query("" +
                  "SELECT " +
                  "   co.dateTime, " +
                  "   co.occupied, " +
                  "   co.classroomNumber " +
                  "FROM mm_ClassroomOccupancy co " +
                  "   JOIN ( " +
                  "           SELECT " +
                  "              MAX(co_inner.dateTime) AS dateTime, " +
                  "              co_inner.classroomNumber " +
                  "           FROM mm_ClassRoomOccupancy co_inner " +
                  "           GROUP BY co_inner.classRoomNumber " +
                  "        ) AS latest ON latest.classRoomNumber = co.classRoomNumber " +
                  "                       AND latest.dateTime = co.dateTime " +
                  "ORDER BY co.dateTime DESC",
            Collections.emptyMap(),
            ROW_MAPPER
      );


      long numberOfOccupied = result.stream()
            .filter(ClassroomOccupancy::isOccupied)
            .count();

      LOG.info("Loaded [occupied={}, free={}, duration={}]",
            numberOfOccupied,
            result.size() - numberOfOccupied,
            TimeUtils.duration(opStart)
      );

      return result;
   }

   public List<ClassroomOccupancy> load(LocalDateTime periodStart, LocalDateTime periodEnd, String text) {
      Map<String, Object> args = new HashMap<>();
      args.put("periodStart", periodStart);
      args.put("periodEnd", periodEnd);
      args.put("classroomNumber", StringUtils.stripToNull(text));

      return jdbcSalto.query("" +
                  "SELECT " +
                  "   dateTime, " +
                  "   occupied, " +
                  "   classroomNumber " +
                  "FROM mm_ClassroomOccupancy " +
                  "WHERE (:periodStart IS NULL OR :periodStart <= dateTime) " +
                  "  AND (:periodEnd IS NULL OR :periodEnd >= dateTime) " +
                  "  AND (:classroomNumber IS NULL OR :classroomNumber = classRoomNumber) " +
                  "ORDER BY dateTime DESC",
            args,
            ROW_MAPPER
      );
   }

   public void save(int number, boolean occupied) {
      jdbcSalto.update(
            "INSERT INTO mm_ClassroomOccupancy (classroomNumber, occupied) VALUES (:number, :operation)",
            ImmutableMap.of("number", number, "operation", occupied)
      );
   }
}
