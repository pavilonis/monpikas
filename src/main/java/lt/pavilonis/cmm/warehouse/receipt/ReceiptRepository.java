package lt.pavilonis.cmm.warehouse.receipt;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReceiptRepository implements EntityRepository<Receipt, Long, ReceiptFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Transactional
   @Override
   public Receipt saveOrUpdate(Receipt entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", entity.getId());
      args.put("supplierId", entity.getSupplier().getId());

      return entity.getId() == null
            ? save(entity, args)
            : update(entity, args);
   }

   private Receipt update(Receipt entity, Map<String, Object> args) {

      jdbcNamed.update("UPDATE Receipt SET supplier_id = :supplierId WHERE id = :id", args);
      jdbcNamed.update("DELETE FROM ReceiptItem WHERE receipt_id = :id", args);

      saveItems(entity.getId(), entity.getItems());

      return find(entity.getId())
            .orElseThrow(() -> new RuntimeException("Could not load updated object"));
   }

   private Receipt save(Receipt entity, Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update(
            "INSERT INTO Receipt (supplier_id) VALUE (:supplierId)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      long receiptId = keyHolder.getKey().longValue();

      saveItems(receiptId, entity.getItems());

      return find(receiptId)
            .orElseThrow(() -> new RuntimeException("Could not load saved object"));
   }

   private void saveItems(long receiptId, Collection<ReceiptItem> items) {

      @SuppressWarnings("unchecked")
      Map<String, ?>[] batchArgs = items.stream()
            .map(item -> ImmutableMap.builder()
                  .put("receiptId", receiptId)
                  .put("productId", item.getProduct().getId())
                  .put("unitPrice", item.getUnitPrice())
                  .put("quantity", item.getQuantity())
                  .put("productNameSnapshot", item.getProduct().getName())
                  .put("productMeasureUnitSnapshot", item.getProduct().getMeasureUnit().name())
                  .put("productUnitWeightSnapshot", item.getProduct().getUnitWeight())
                  .build())
            .toArray(Map[]::new);

      jdbcNamed.batchUpdate("" +
                  "INSERT INTO ReceiptItem (" +
                  "  receipt_id, product_id, unitPrice, quantity, " +
                  "  productNameSnapshot, productMeasureUnitSnapshot, productUnitWeightSnapshot" +
                  ") VALUES (" +
                  "  :receiptId, :productId, :unitPrice, :quantity, " +
                  "  :productNameSnapshot, :productMeasureUnitSnapshot, :productUnitWeightSnapshot" +
                  ")",
            batchArgs
      );
   }

   @Override
   public List<Receipt> load() {
      return load(new ReceiptFilter(null, null, null, null));
   }

   @Override
   public List<Receipt> load(ReceiptFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("supplierId", filter.getSupplier() == null ? null : filter.getSupplier().getId());
      args.put("id", filter.getId());
      return jdbcNamed.query("" +
                  "SELECT s.id, s.name, s.code," +
                  "  r.id, r.dateCreated, " +
                  "  ri.id, ri.unitPrice, ri.quantity," +
                  "  p.id, p.name, p.measureUnit, p.unitWeight, " +
                  "  pg.id, pg.name, pg.kcal100 " +
                  "FROM Receipt r " +
                  "  JOIN Supplier s ON s.id = r.supplier_id " +
                  "  JOIN ReceiptItem ri ON ri.receipt_id = r.id " +
                  "  JOIN Product p ON p.id = ri.product_id " +
                  "  JOIN ProductGroup pg ON pg.id = p.productGroup_id " +
                  "WHERE (:id IS NULL OR :id = r.id) " +
                  "  AND (:supplierId IS NULL OR :supplierId = s.id)",
            args,
            new ReceiptResultSetExtractor()
      );
   }

   @Override
   public Optional<Receipt> find(Long id) {
      List<Receipt> result = load(new ReceiptFilter(id, null, null, null));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM Receipt WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<Receipt> entityClass() {
      return Receipt.class;
   }
}
