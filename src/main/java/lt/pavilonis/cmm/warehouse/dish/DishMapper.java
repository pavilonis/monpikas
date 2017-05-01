package lt.pavilonis.cmm.warehouse.dish;

import lt.pavilonis.cmm.warehouse.dishGroup.DishGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DishMapper implements RowMapper<Dish> {

   @Override
   public Dish mapRow(ResultSet rs, int i) throws SQLException {
      return new Dish(
            rs.getLong("d.id"),
            rs.getString("d.name"),
            new DishGroup(rs.getLong("dg.id"), rs.getString("dg.name"))
      );
   }
}
