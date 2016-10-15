package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

@Repository
public class MealRepository {

   private final MealMapper MAPPER = new MealMapper();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public List<Meal> loadAll() {
      return jdbc.query("SELECT m.* FROM Meal m", MAPPER);
   }

   public void delete(long mealId) {
      jdbc.update("DELETE FROM Meal WHERE id = ?", mealId);
   }

   public List<Meal> load(Set<Long> ids) {
      return namedJdbc.query(
            "SELECT m.* FROM Meal m WHERE m.id IN (:ids)",
            Collections.singletonMap("ids", ids),
            MAPPER
      );
   }

   public Meal saveOrUpdate(Meal meal) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", meal.getId());
      args.put("name", meal.getName());
      args.put("type", meal.getType().name());
      args.put("price", meal.getPrice());
      args.put("startTime", meal.getStartTime());
      args.put("endTime", meal.getEndTime());

      return isNull(meal.getId())
            ? save(args)
            : update(args);
   }

   private Meal save(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      namedJdbc.update(
            "INSERT INTO Meal (name, type, price, startTime, endTime) " +
                  "VALUES (:name, :type, :price, :startTime, :endTime)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      Set<Long> singleKey = Collections.singleton(keyHolder.getKey().longValue());
      return load(singleKey).get(0);
   }

   private Meal update(Map<String, Object> args) {
      jdbc.update("" +
                  "UPDATE " +
                  "  Meal " +
                  "SET " +
                  "  name = :name, " +
                  "  type = :type, " +
                  "  price = :price, " +
                  "  startTime = :startTime, " +
                  "  endTime = :endTime " +
                  "WHERE id = :id"
      );
      long mealId = (long) args.get("id");
      return load(Collections.singleton(mealId)).get(0);
   }
}
