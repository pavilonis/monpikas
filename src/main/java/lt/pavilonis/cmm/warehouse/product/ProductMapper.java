package lt.pavilonis.cmm.warehouse.product;

import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

   private static final RowMapper<ProductGroup> PRODUCT_GROUP_MAPPER = new ProductGroupMapper();

   @Override
   public Product mapRow(ResultSet rs, int i) throws SQLException {
      return new Product(
            rs.getLong("p.id"),
            rs.getString("p.name"),
            MeasureUnit.valueOf(rs.getString("p.measureUnit")),
            rs.getInt("p.unitWeight"),
            PRODUCT_GROUP_MAPPER.mapRow(rs, i)
      );
   }
}
