package lt.pavilonis.cmm.warehouse.meal;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;
import lt.pavilonis.cmm.warehouse.mealtype.MealTypeMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class MealMapper extends SimpleRowMapper<Meal> {

   private static final MealTypeMapper MEAL_TYPE_MAPPER = new MealTypeMapper();

   @Override
   public Meal mapRow(ResultSet rs, int i) throws SQLException {
      return new Meal(
            rs.getLong("m.id"),
            MEAL_TYPE_MAPPER.mapRow(rs, i),
            new ArrayList<>()
      );
   }
}
