package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.warehouse.product.ProductMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WriteOffResultSetExtractor implements ResultSetExtractor<List<WriteOff>> {

   private final static ProductMapper MAPPER_PRODUCT = new ProductMapper();

   @Override
   public List<WriteOff> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, WriteOff> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("r.id");
         WriteOff receipt = result.get(id);

         if (receipt == null) {
            result.put(id, receipt = mapRow(rs));
         }

         maybeAddReceiptItem(rs, receipt.getItems());
      }
      return new ArrayList<>(result.values());
   }

   public static void maybeAddReceiptItem(ResultSet rs, Collection<WriteOffItem> items) throws SQLException {

      Long itemId = (Long) rs.getObject("woi.id");

      if (itemId != null
            && items.stream().noneMatch(item -> itemId.equals(item.getId()))) {

         items.add(new WriteOffItem(
               itemId,
               null, //TODO
               rs.getBigDecimal("woi.quantity")
         ));
      }
   }

   public static WriteOff mapRow(ResultSet rs) throws SQLException {
      return new WriteOff(
            rs.getLong("wo.id"),
            rs.getDate("wo.periodStart").toLocalDate(),
            rs.getDate("wo.periodEnd").toLocalDate(),
            rs.getTimestamp("r.dateCreated").toLocalDateTime()
      );
   }
}
