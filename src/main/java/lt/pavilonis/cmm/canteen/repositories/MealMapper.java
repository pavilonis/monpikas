package lt.pavilonis.cmm.canteen.repositories;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MealMapper implements RowMapper<Meal> {
   @Override
   public Meal mapRow(ResultSet rs, int i) throws SQLException {
      return new Meal(
            rs.getLong("m.id"),
            rs.getString("m.name"),
            MealType.valueOf(rs.getString("m.type")),
            rs.getBigDecimal("m.price"),
            rs.getTimestamp("m.startTime").toLocalDateTime().toLocalTime(),
            rs.getTimestamp("m.endTime").toLocalDateTime().toLocalTime()
      );
   }
}
