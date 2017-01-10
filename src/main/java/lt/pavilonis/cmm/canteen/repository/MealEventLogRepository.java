package lt.pavilonis.cmm.canteen.repository;


import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MealEventLogRepository {

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

   public MealEventLog load(long id) {
      return jdbc.queryForObject("SELECT * FROM MealEventLog WHERE id = ?", MAPPER, id);
   }

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

   public void delete(long id) {
      jdbc.update("DELETE FROM MealEventLog WHERE id = ?", id);
   }

   public MealEventLog save(String cardCode, String name, String grade,
                            BigDecimal price, MealType mealType, PupilType pupilType) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("name", name);
      args.put("price", price);
      args.put("mealType", mealType.name());
      args.put("grade", grade);
      args.put("pupilType", pupilType.name());
      KeyHolder keyHolder = new GeneratedKeyHolder();

      namedJdbc.update(
            "INSERT INTO MealEventLog (cardCode, `name`, price, mealType, grade, pupilType) " +
                  "VALUES (:cardCode, :name, :price, :mealType, :grade, :pupilType)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return load(keyHolder.getKey().longValue());
   }
}