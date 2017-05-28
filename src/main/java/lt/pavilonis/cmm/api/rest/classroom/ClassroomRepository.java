package lt.pavilonis.cmm.api.rest.classroom;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.school.classroom.ClassroomFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ClassroomRepository implements EntityRepository<ClassroomOccupancy, Void, ClassroomFilter> {

   private static final RowMapper<ClassroomOccupancy> ROW_MAPPER =
         (rs, i) -> new ClassroomOccupancy(
               rs.getTimestamp(1).toLocalDateTime(),
               rs.getBoolean(2),
               rs.getInt(3)
         );

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   public List<ClassroomOccupancy> loadActive() {

      return jdbcSalto.query("" +
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
   }

   @Override
   public List<ClassroomOccupancy> load(ClassroomFilter filter) {

      if (!filter.isHistoryMode()) {
         return loadActive();
      }

      Map<String, Object> args = new HashMap<>();
      args.put("periodStart", filter.getPeriodStart().atTime(LocalTime.MIN));
      args.put("periodEnd", filter.getPeriodEnd().atTime(LocalTime.MAX));
      args.put("classroomNumber", StringUtils.stripToNull(filter.getText()));


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

   @Override
   public ClassroomOccupancy saveOrUpdate(ClassroomOccupancy entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Optional<ClassroomOccupancy> find(Void s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Void s) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<ClassroomOccupancy> entityClass() {
      return ClassroomOccupancy.class;
   }
}
