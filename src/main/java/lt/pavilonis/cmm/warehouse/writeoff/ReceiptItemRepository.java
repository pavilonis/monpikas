package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiptItemRepository {

   @Autowired
   private JdbcTemplate jdbc;

   ReceiptItem load(long receiptItemId) {
      return jdbc.queryForObject("" +
                  "SELECT " +
                  "   ri.id, ri.quantity, ri.unitPrice, " +
                  "   p.id, p.name, p.measureUnit, p.unitWeight, " +
                  "   pg.id, pg.name, pg.kcal100 " +
                  "FROM ReceiptItem ri " +
                  "  JOIN Product p ON p.id = ri.product_id " +
                  "  JOIN ProductGroup pg ON pg.id = p.productGroup_id " +
                  "WHERE ri.id = ?",
            (rs, i) -> {
               ProductGroup productGroup = new ProductGroup(
                     rs.getLong("pg.id"),
                     rs.getString("pg.name"),
                     rs.getInt("pg.kcal100")
               );
               Product product = new Product(
                     rs.getLong("p.id"),
                     rs.getString("p.name"),
                     MeasureUnit.valueOf(rs.getString("p.measureUnit")),
                     rs.getInt("p.unitWeight"),
                     productGroup
               );
               return new ReceiptItem(
                     rs.getLong("ri.id"),
                     product,
                     rs.getBigDecimal("ri.unitPrice"),
                     rs.getBigDecimal("ri.quantity")
               );
            },
            receiptItemId
      );
   }
}
