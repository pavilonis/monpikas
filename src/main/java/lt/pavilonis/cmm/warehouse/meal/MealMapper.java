package lt.pavilonis.cmm.warehouse.meal;

import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MealMapper implements RowMapper<Meal> {


   @Override
   public Meal mapRow(ResultSet rs, int i) throws SQLException {
      return null;
//      return new Product(
//            rs.getLong("p.id"),
//            rs.getString("p.name"),
//            MeasureUnit.valueOf(rs.getString("p.measureUnit")),
//            rs.getInt("p.unitWeight"),
//            PRODUCT_GROUP_MAPPER.mapRow(rs, i)
//      );
   }
}
