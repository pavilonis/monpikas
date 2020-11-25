package lt.pavilonis.cmm.api.rest.classroom;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.tcp.Classroom;
import lt.pavilonis.cmm.common.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClassroomRepository {

   private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomRepository.class.getSimpleName());
   private static final RowMapper<ClassroomOccupancy> ROW_MAPPER = (rs, i) -> new ClassroomOccupancy(
         rs.getTimestamp(1).toLocalDateTime(),
         rs.getBoolean(2),
         rs.getInt(3),
         rs.getString(4)
   );

   private final NamedParameterJdbcTemplate jdbcSalto;

   public ClassroomRepository(NamedParameterJdbcTemplate jdbcSalto) {
      this.jdbcSalto = jdbcSalto;
   }

   public List<ClassroomOccupancy> loadActive(List<Integer> levels, String building) {

      LocalDateTime opStart = LocalDateTime.now();
      Map<String, Object> params = new HashMap<>();
      params.put("building", StringUtils.stripToNull(building));
      params.put("levels", levels.isEmpty() ? null : levels);

      List<ClassroomOccupancy> result = jdbcSalto.query("" +
                  "SELECT " +
                  "  occ.dateTime, " +
                  "  occ.occupied, " +
                  "  occ.classroomNumber, " +
                  "  occ.building " +
                  "FROM mm_ClassroomOccupancy occ " +
                  "  JOIN ( " +
                  "          SELECT " +
                  "             MAX(occ_inner.dateTime) AS dateTime, " +
                  "             occ_inner.classroomNumber, " +
                  "             occ_inner.building " +
                  "          FROM mm_ClassroomOccupancy occ_inner " +
                  "          WHERE (:building IS NULL OR occ_inner.building = :building) " +
                  (levels.isEmpty() ? "" : "AND occ_inner.classroomNumber / 100 IN(:levels) ") +
                  "          GROUP BY occ_inner.building, occ_inner.classRoomNumber " +
                  "       ) AS occ_latest ON occ_latest.building = occ.building " +
                  "                      AND occ_latest.classRoomNumber = occ.classRoomNumber " +
                  "                      AND occ_latest.dateTime = occ.dateTime " +
                  "ORDER BY occ.dateTime DESC",
            params,
            ROW_MAPPER
      );

      long free = result.stream()
            .filter(ClassroomOccupancy::isOccupied)
            .count();

      LOGGER.info("Loaded [occupied={}/{}, building={}, levels={}, t={}]",
            free, result.size(), building, levels, TimeUtils.duration(opStart));

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

   public void save(Classroom classroom, boolean occupied, LocalDateTime dateTime) {
      jdbcSalto.update(
            "INSERT INTO mm_ClassroomOccupancy (classroomNumber, building, occupied, dateTime) " +
                  "VALUES (:number, :building, :operation, :dateTime)",
            ImmutableMap.of(
                  "number", classroom.getClassNumber(),
                  "building", classroom.getBuilding().name(),
                  "operation", occupied,
                  "dateTime", Timestamp.valueOf(dateTime)
            )
      );
   }
}
