package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.PupilType;
import org.apache.commons.lang3.NotImplementedException;
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

   private final RowMapper<MealEventLog> ROW_MAPPER = (rs, i) -> new MealEventLog(
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
      return jdbc.queryForObject("SELECT * FROM MealEventLog WHERE id = ?", ROW_MAPPER, id);
   }

   public Date lastMealEventDate(String cardCode) {
      return jdbc.queryForObject("SELECT max(date) FROM MealEventLog WHERE cardCode = ?", Date.class, cardCode);
   }

   public List<Long> todaysMealEvents() {
//   @Query("select m.cardId from MealEventLog m where m.date > CURDATE()")
      throw new NotImplementedException("TODO");
   }

   public Long numOfTodaysMealEventsByCardId(String cardCode, Date checkDate) {
//   @Query("select count(m.cardId) from MealEventLog m where m.date > :checkDate and m.cardId = :cardId")
      throw new NotImplementedException("TODO");
   }

   public int numOfMealEvents(String cardCode, Date periodStart, Date periodEnd, MealType mealType) {
      return jdbc.queryForObject(
            "SELECT count(*) FROM MealEventLog WHERE `date` BETWEEN ? AND ? AND cardCode = ? AND mealType= ?",
            Integer.class,
            periodStart, periodEnd, cardCode, mealType
      );
   }

   public List<MealEventLog> loadAfter(Date periodStart) {
      return jdbc.query("SELECT * FROM MealEventLog WHERE `date` > ? ORDER BY `date` DESC", ROW_MAPPER, periodStart);
   }

   public List<MealEventLog> load(Date periodStart, Date periodEnd, PupilType pupilType) {
      return jdbc.query(
            "SELECT * FROM MealEventLog WHERE pupilType = ? AND `date` BETWEEN ? AND ?",
            ROW_MAPPER,
            pupilType, periodStart, periodEnd
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