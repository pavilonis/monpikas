package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.views.setting.MealFilter;
import lt.pavilonis.cmm.canteen.views.user.UserMealFilter;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static java.util.Objects.isNull;

@Repository
public class MealRepository implements EntityRepository<Meal, Long, MealFilter> {

   private final MealMapper MAPPER = new MealMapper();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;


   @Override
   public List<Meal> loadAll(MealFilter filter) {
      return jdbc.query("SELECT m.* FROM Meal m", MAPPER);
   }

   @Override
   public void delete(Long mealId) {
      jdbc.update("DELETE FROM Meal WHERE id = ?", mealId);
   }

   public List<Meal> load(Collection ids) {
      return CollectionUtils.isEmpty(ids)
            ? Collections.emptyList()
            : namedJdbc.query("SELECT m.* FROM Meal m WHERE m.id IN (:ids)", singletonMap("ids", ids), MAPPER);
   }

   @Override
   public Optional<Meal> load(Long id) {
      List<Meal> result = jdbc.query("SELECT m.* FROM Meal m WHERE m.id = ?", MAPPER, id);

      return result.isEmpty()
            ? Optional.<Meal>empty()
            : Optional.of(result.get(0));
   }

   @Override
   public Meal saveOrUpdate(Meal meal) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", meal.getId());
      args.put("mealName", meal.getName());
      args.put("type", meal.getType().name());
      args.put("price", meal.getPrice());
      args.put("startTime", minutes(meal.getStartTime()));
      args.put("endTime", minutes(meal.getEndTime()));

      return isNull(meal.getId())
            ? save(args)
            : update(args);
   }

   protected long minutes(LocalTime localTime) {
      return localTime == null
            ? 0
            : ChronoUnit.MINUTES.between(LocalTime.MIN, localTime);
   }

   private Meal save(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      namedJdbc.update(
            "INSERT INTO Meal (name, type, price, startTime, endTime) " +
                  "VALUES (:mealName, :type, :price, :startTime, :endTime)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      Set<Long> singleKey = Collections.singleton(keyHolder.getKey().longValue());
      return load(singleKey).get(0);
   }

   private Meal update(Map<String, Object> args) {
      namedJdbc.update("" +
                  "UPDATE " +
                  "  Meal " +
                  "SET " +
                  "  `name` = :mealName, " +
                  "  `type` = :type, " +
                  "  `price` = :price, " +
                  "  `startTime` = :startTime, " +
                  "  `endTime` = :endTime " +
                  "WHERE id = :id ",
            args
      );
      long mealId = (long) args.get("id");
      return load(Collections.singleton(mealId)).get(0);
   }
}
