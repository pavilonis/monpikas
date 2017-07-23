package lt.pavilonis.cmm.warehouse.receipt;

import lt.pavilonis.cmm.warehouse.product.ProductMapper;
import lt.pavilonis.cmm.warehouse.supplier.SupplierRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReceiptResultSetExtractor implements ResultSetExtractor<List<Receipt>> {

   private final static ProductMapper MAPPER_PRODUCT = new ProductMapper();
   private final static SupplierRowMapper MAPPER_SUPPLIER = new SupplierRowMapper();

   @Override
   public List<Receipt> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, Receipt> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("r.id");
         Receipt receipt = result.get(id);
         if (receipt == null) {
            result.put(id, receipt = mapRow(rs));
         }

         maybeAddReceiptItem(rs, receipt.getItems());
      }
      return new ArrayList<>(result.values());
   }

   public static void maybeAddReceiptItem(ResultSet rs, Collection<ReceiptItem> items) throws SQLException {

      Long itemId = (Long) rs.getObject("ri.id");

      if (itemId != null
            && items.stream().noneMatch(item -> itemId.equals(item.getId()))) {

         items.add(new ReceiptItem(
               itemId,
               MAPPER_PRODUCT.mapRow(rs),
               rs.getBigDecimal("ri.unitPrice"),
               rs.getBigDecimal("ri.quantity")
         ));
      }
   }

   public static Receipt mapRow(ResultSet rs) throws SQLException {
      return new Receipt(
            rs.getLong("r.id"),
            MAPPER_SUPPLIER.mapRow(rs),
            rs.getTimestamp("r.dateCreated"),
            new ArrayList<>()
      );
   }
}
