package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;
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

   private final ReceiptItemMapper receiptItemMapper = new ReceiptItemMapper();

   @Override
   public List<WriteOff> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, WriteOff> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("wo.id");
         WriteOff writeOff = result.get(id);

         if (writeOff == null) {
            result.put(id, writeOff = mapRow(rs));
         }

         maybeAddReceiptItem(rs, writeOff.getItems());
      }
      return new ArrayList<>(result.values());
   }

   public void maybeAddReceiptItem(ResultSet rs, Collection<WriteOffItem> items) throws SQLException {

      Long itemId = (Long) rs.getObject("woi.id");

      if (itemId != null
            && items.stream().noneMatch(item -> itemId.equals(item.getId()))) {

         ReceiptItem receiptItem = receiptItemMapper.mapRow(rs);

         WriteOffItem item = new WriteOffItem(
               itemId,
               receiptItem,
               rs.getBigDecimal("woi.quantityAvailableBefore"),
               rs.getBigDecimal("woi.quantityConsumed"),
               rs.getBigDecimal("woi.quantity"),
               rs.getBigDecimal("woi.quantityAvailableAfter"),
               receiptItem.getProduct().getProductGroup()
         );
         items.add(item);
      }
   }

   public static WriteOff mapRow(ResultSet rs) throws SQLException {
      return new WriteOff(
            rs.getLong("wo.id"),
            rs.getDate("wo.periodStart").toLocalDate(),
            rs.getDate("wo.periodEnd").toLocalDate(),
            rs.getTimestamp("wo.dateCreated").toLocalDateTime()
      );
   }
}
