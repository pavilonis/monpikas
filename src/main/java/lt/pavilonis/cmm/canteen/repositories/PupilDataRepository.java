package lt.pavilonis.cmm.canteen.repositories;

import lt.pavilonis.TimeUtils;
import lt.pavilonis.cmm.canteen.domain.MealData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class PupilDataRepository {

   private static final Logger LOG = LoggerFactory.getLogger(PupilDataRepository.class.getSimpleName());
   private static final PupilDataResultSetExtractor PUPIL_DATA_EXTRACTOR = new PupilDataResultSetExtractor();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public Collection<MealData> loadByMeal(long mealId) {
      Map<String, MealData> result = query(null, mealId, false);
      return result.values();
   }

   public Collection<MealData> loadAll(boolean withMealsAssigned) {
      return query(null, null, withMealsAssigned).values();
   }

   public Optional<MealData> load(String cardCode) {
      Map<String, MealData> result = query(cardCode, null, false);
      return result.isEmpty()
            ? Optional.<MealData>empty()
            : Optional.of(result.values().iterator().next());
   }

   private Map<String, MealData> query(String cardCode, Long mealId, boolean withMealsAssigned) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("mealId", mealId);
      args.put("withMealsAssigned", withMealsAssigned);
      LocalDateTime opStart = LocalDateTime.now();
      Map<String, MealData> result = namedJdbc.query("" +
                  "SELECT " +
                  "  p.cardCode, p.comment, p.type, " +
                  "  m.id, m.name, m.type, m.price, m.startTime, m.endTime " +
                  "FROM Pupil p" +
                  "  LEFT JOIN PupilMeal pm ON pm.pupil_cardCode = p.cardCode " +
                  "  LEFT JOIN Meal m ON m.id = pm.meal_id " +
                  "WHERE (:cardCode IS NULL OR :cardCode = p.cardCode)" +
                  "  AND (:mealId IS NULL OR :mealId = m.id) " +
                  "  AND (:withMealsAssigned IS FALSE OR m.id IS NOT NULL)",
            args,
            PUPIL_DATA_EXTRACTOR
      );
      LOG.info("Query completed [duration={}]", TimeUtils.duration(opStart));

      return result;
   }

   @Transactional
   public MealData saveOrUpdate(MealData pupil) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", pupil.getCardCode());
      args.put("type", pupil.getType().name());
      args.put("comment", pupil.getComment());

      namedJdbc.update("" +
                  "INSERT INTO Pupil (cardCode, type, comment) " +
                  "VALUES (:cardCode, :type, :comment) " +
                  "ON DUPLICATE KEY UPDATE cardCode = :cardCode, type = :type, comment = :comment",
            args
      );

      jdbc.update("DELETE FROM PupilMeal WHERE pupil_cardCode = ?", pupil.getCardCode());

      List<Object[]> batchArgs = pupil.getMeals().stream()
            .map(meal -> new Object[]{pupil.getCardCode(), meal.getId()})
            .collect(toList());

      if (!batchArgs.isEmpty()) {
         jdbc.batchUpdate("INSERT INTO PupilMeal (pupil_cardCode, meal_id) VALUES (?, ?)", batchArgs);
      }

      return load(pupil.getCardCode()).get();
   }
}