package lt.pavilonis.cmm.warehouse.mealtype;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MealTypeMapper extends SimpleRowMapper<MealType> {

   @Override
   public MealType mapRow(ResultSet rs, int i) throws SQLException {

      return new MealType(
            rs.getLong("mt.id"),
            rs.getString("mt.name")
      );
   }
}
