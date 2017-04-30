package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class EatingMapper implements RowMapper<Eating> {
   @Override
   public Eating mapRow(ResultSet rs, int i) throws SQLException {
      return new Eating(
            rs.getLong("e.id"),
            rs.getString("e.name"),
            EatingType.valueOf(rs.getString("e.type")),
            rs.getBigDecimal("e.price"),
            LocalTime.MIN.plusMinutes(rs.getInt("e.startTime")),
            LocalTime.MIN.plusMinutes(rs.getInt("e.endTime"))
      );
   }
}
