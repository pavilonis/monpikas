package lt.pavilonis.cmm.warehouse.productgroup;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProductGroupMapper extends SimpleRowMapper<ProductGroup> {
   @Override
   public ProductGroup mapRow(ResultSet rs, int i) throws SQLException {
      return new ProductGroup(
            rs.getLong("pg.id"),
            rs.getString("pg.name"),
            rs.getInt("pg.kcal100")
      );
   }
}
