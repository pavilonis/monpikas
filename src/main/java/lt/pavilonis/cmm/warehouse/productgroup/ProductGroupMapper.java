package lt.pavilonis.cmm.warehouse.productgroup;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductGroupMapper implements RowMapper<ProductGroup> {
   @Override
   public ProductGroup mapRow(ResultSet rs, int i) throws SQLException {
      return new ProductGroup(rs.getLong("pg.id"), rs.getString("pg.name"), rs.getInt("pg.kcal100"));
   }
}
