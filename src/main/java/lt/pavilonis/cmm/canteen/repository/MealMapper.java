package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class MealMapper implements RowMapper<Meal> {
   @Override
   public Meal mapRow(ResultSet rs, int i) throws SQLException {
      return new Meal(
            rs.getLong("m.id"),
            rs.getString("m.name"),
            MealType.valueOf(rs.getString("m.type")),
            rs.getBigDecimal("m.price"),
            LocalTime.MIN.plusMinutes(rs.getInt("m.startTime")),
            LocalTime.MIN.plusMinutes(rs.getInt("m.endTime"))
      );
   }
}
