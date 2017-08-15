package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;
import lt.pavilonis.cmm.warehouse.product.ProductMapper;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceiptItemMapper extends SimpleRowMapper<ReceiptItem> {

   private final ProductMapper productMapper = new ProductMapper();

   @Override
   public ReceiptItem mapRow(ResultSet rs, int i) throws SQLException {
      return new ReceiptItem(
            rs.getLong("ri.id"),
            productMapper.mapRow(rs),
            rs.getBigDecimal("ri.unitPrice"),
            rs.getBigDecimal("ri.quantity")
      );
   }
}
