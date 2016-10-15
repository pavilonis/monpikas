package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.domain.PupilLocalData;
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

   public Collection<PupilLocalData> loadByMeal(long mealId) {
      Map<String, PupilLocalData> result = query(null, mealId, false);
      return result.values();
   }

   public Collection<PupilLocalData> loadAll(boolean withMealsAssigned) {
      return query(null, null, withMealsAssigned).values();
   }

   public Optional<PupilLocalData> load(String cardCode) {
      Map<String, PupilLocalData> result = query(cardCode, null, false);
      return result.isEmpty()
            ? Optional.<PupilLocalData>empty()
            : Optional.of(result.values().iterator().next());
   }

   private Map<String, PupilLocalData> query(String cardCode, Long mealId, boolean withMealsAssigned) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", cardCode);
      args.put("mealId", mealId);
      args.put("withMealsAssigned", withMealsAssigned);
      return namedJdbc.query("" +
                  "SELECT p.*, m.* FROM Pupil p" +
                  "  LEFT JOIN PupilMeal pm ON pm.pupil_cardCode = p.cardCode " +
                  "  LEFT JOIN Meal m ON m.id = pm.meal_id " +
                  "WHERE (:cardCode IS NULL OR :cardCode = p.cardCode)" +
                  "  AND (:mealId IS NULL OR :mealId = m.id) " +
                  "  AND (:withMealsAssigned IS FALSE OR m.id IS NOT NULL)",
            args,
            PUPIL_DATA_EXTRACTOR
      );
   }

   @Transactional
   public PupilLocalData saveOrUpdate(Pupil pupil) {
      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", pupil.cardCode);
      args.put("type", pupil.pupilType);
      args.put("comment", pupil.comment);

      namedJdbc.update("" +
                  "INSERT INTO Pupil (cardCode, type, comment) " +
                  "VALUES (:cardCode, :type, :comment) " +
                  "ON DUPLICATE KEY UPDATE cardCode = :cardCode, type = :type, comment = :comment",
            args
      );

      jdbc.update("DELETE FROM PupilMeal WHERE pupil_cardCode = ?", pupil.cardCode);

      List<Object[]> batchArgs = pupil.meals.stream()
            .map(meal -> new Object[]{pupil.cardCode, meal.getId()})
            .collect(toList());

      if (!batchArgs.isEmpty()) {
         jdbc.batchUpdate("INSERT INTO PupilMeal (pupil_cardCode, meal_id) VALUES (?, ?)", batchArgs);
      }

      return load(pupil.cardCode).get();
   }
}