package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.MealData;
import lt.pavilonis.cmm.canteen.domain.MealType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class PupilDataRepository {

   private static final PupilDataResultSetExtractor PUPIL_DATA_EXTRACTOR = new PupilDataResultSetExtractor();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   public Collection<MealData> loadAll(boolean withMealsAssignedOnly, MealType mealType) {
      return query(null, mealType, withMealsAssignedOnly).values();
   }

   public Optional<MealData> load(String cardCode) {
      Map<String, MealData> result = query(cardCode, null, false);
      return result.isEmpty()
            ? Optional.<MealData>empty()
            : Optional.of(result.values().iterator().next());
   }

   private Map<String, MealData> query(String cardCode, MealType mealType, boolean withMealsAssigned) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("withMealsAssigned", withMealsAssigned);
      args.put("mealType", mealType == null ? null : mealType.name());
      return namedJdbc.query("" +
                  "SELECT " +
                  "  p.cardCode, p.comment, p.type, " +
                  "  m.id, m.name, m.type, m.price, m.startTime, m.endTime " +
                  "FROM Pupil p" +
                  "  LEFT JOIN PupilMeal pm ON pm.pupil_cardCode = p.cardCode " +
                  "  LEFT JOIN Meal m ON m.id = pm.meal_id " +
                  "WHERE (:cardCode IS NULL OR :cardCode = p.cardCode)" +
                  "  AND (:mealType IS NULL OR m.type = :mealType)" +
                  "  AND (:withMealsAssigned IS FALSE OR m.id IS NOT NULL)",
            args,
            PUPIL_DATA_EXTRACTOR
      );
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