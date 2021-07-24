package lt.pavilonis.monpikas.common.util;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SimpleRowMapper<T> implements RowMapper<T> {

   public T mapRow(ResultSet rs) {
      try {
         return mapRow(rs, 0);
      } catch (SQLException e) {
         throw new RuntimeException("Could not map a row", e);
      }
   }

}
