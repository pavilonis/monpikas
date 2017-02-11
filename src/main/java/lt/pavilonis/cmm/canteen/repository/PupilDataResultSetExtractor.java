package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealData;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PupilDataResultSetExtractor implements ResultSetExtractor<Map<String, MealData>> {

   private final int ANY = 100500;
   private final MealMapper MEAL_MAPPER = new MealMapper();

   @Override
   public Map<String, MealData> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<String, MealData> result = new HashMap<>();
      while (rs.next()) {
         String cardCode = rs.getString("p.cardCode");
         MealData pupilData = result.get(cardCode);
         if (pupilData == null) {
            pupilData = new MealData(
                  cardCode,
                  PupilType.valueOf(rs.getString("p.type")),
                  rs.getString("p.comment"),
                  new HashSet<>()
            );
            result.put(cardCode, pupilData);
         }

         Long mealId = (Long) rs.getObject("m.id");
         if (mealId != null) {
            Meal meal = MEAL_MAPPER.mapRow(rs, ANY);
            pupilData.getMeals().add(meal);
         }
      }
      return result;
   }
}
