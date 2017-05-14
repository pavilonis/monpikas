package lt.pavilonis.cmm.warehouse.meal;

import lt.pavilonis.cmm.warehouse.mealtype.MealTypeMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class MealMapper implements RowMapper<Meal> {

   private static final MealTypeMapper MEAL_TYPE_MAPPER = new MealTypeMapper();

   @Override
   public Meal mapRow(ResultSet rs, int i) throws SQLException {
      return new Meal(
            rs.getLong("m.id"),
            MEAL_TYPE_MAPPER.mapRow(rs, i),
            new ArrayList<>()
      );
   }

   public Meal mapRow(ResultSet rs) {
      try {
         return mapRow(rs, 0);
      } catch (SQLException e) {
         throw new RuntimeException("Could not map a row");
      }
   }
}
