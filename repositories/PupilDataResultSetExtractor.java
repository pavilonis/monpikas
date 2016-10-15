package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.PupilLocalData;
import lt.pavilonis.monpikas.server.domain.PupilType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PupilDataResultSetExtractor implements ResultSetExtractor<Map<String, PupilLocalData>> {

   private final int ANY = 100500;
   private final MealMapper MEAL_MAPPER = new MealMapper();

   @Override
   public Map<String, PupilLocalData> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<String, PupilLocalData> result = new HashMap<>();

      while (rs.next()) {
         String cardCode = rs.getString("p.cardCode");
         PupilLocalData pupilData = result.get(cardCode);
         if (pupilData == null) {
            pupilData = new PupilLocalData(
                  cardCode,
                  PupilType.valueOf(rs.getString("p.type")),
                  rs.getString("p.comment"),
                  new HashSet<>()
            );
            result.put(cardCode, pupilData);
         }

         Meal meal = MEAL_MAPPER.mapRow(rs, ANY);
         pupilData.getMeals().add(meal);
      }

      return result;
   }
}
