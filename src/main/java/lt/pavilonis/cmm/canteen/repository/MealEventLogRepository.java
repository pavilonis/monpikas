package lt.pavilonis.cmm.canteen.repository;


import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MealEventLogRepository implements EntityRepository<MealEventLog, Long> {

   private final RowMapper<MealEventLog> MAPPER = (rs, i) -> new MealEventLog(
         rs.getLong("id"),
         rs.getString("cardCode"),
         rs.getString("name"),
         rs.getString("grade"),
         rs.getTimestamp("date"),
         rs.getBigDecimal("price"),
         MealType.valueOf(rs.getString("mealType")),
         PupilType.valueOf(rs.getString("pupilType"))
   );

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public Date lastMealEventDate(String cardCode) {
      return jdbc.queryForObject("SELECT max(date) FROM MealEventLog WHERE cardCode = ?", Date.class, cardCode);
   }

   public int numOfMealEvents(String cardCode, Date periodStart, Date periodEnd, MealType mealType) {
      return jdbc.queryForObject(
            "SELECT count(*) FROM MealEventLog WHERE `date` BETWEEN ? AND ? AND cardCode = ? AND mealType= ?",
            Integer.class,
            periodStart, periodEnd, cardCode, mealType
      );
   }

   public List<MealEventLog> loadAfter(Date periodStart) {
      return jdbc.query("SELECT * FROM MealEventLog WHERE `date` > ? ORDER BY `date` DESC", MAPPER, periodStart);
   }

   public List<MealEventLog> load(PupilType pupilType, Date periodStart, Date periodEnd) {
      return jdbc.query(
            "SELECT * FROM MealEventLog WHERE pupilType = ? AND `date` BETWEEN ? AND ?",
            MAPPER,
            pupilType.name(), periodStart, periodEnd
      );
   }

   @Override
   public MealEventLog saveOrUpdate(MealEventLog entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", entity.getCardCode());
      args.put("name", entity.getName());
      args.put("price", entity.getPrice());
      args.put("mealType", entity.getMealType().name());
      args.put("grade", entity.getGrade());
      args.put("pupilType", entity.getPupilType().name());

      KeyHolder keyHolder = new GeneratedKeyHolder();

      namedJdbc.update(
            "INSERT INTO MealEventLog (cardCode, `name`, price, mealType, grade, pupilType) " +
                  "VALUES (:cardCode, :name, :price, :mealType, :grade, :pupilType)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return load(keyHolder.getKey().longValue())
            .orElseThrow(() -> new RuntimeException("could not saved mealEventLog"));
   }

   @Override
   public List<MealEventLog> loadAll() {
      return null;
   }

   @Override
   public Optional<MealEventLog> load(Long id) {
      MealEventLog result = jdbc.queryForObject("SELECT * FROM MealEventLog WHERE id = ?", MAPPER, id);
      return Optional.of(result);
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM MealEventLog WHERE id = ?", id);
   }
}