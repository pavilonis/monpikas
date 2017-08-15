package lt.pavilonis.cmm.warehouse.writeoff;

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
            new ReceiptItemMapper(),
            receiptItemId
      );
   }
}
