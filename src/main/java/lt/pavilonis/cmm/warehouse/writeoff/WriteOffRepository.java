package lt.pavilonis.cmm.warehouse.writeoff;

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
public class WriteOffRepository implements EntityRepository<WriteOff, Long, WriteOffFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Transactional
   @Override
   public WriteOff saveOrUpdate(WriteOff entity) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", entity.getId());
      args.put("supplierId", null);//entity.getSupplier().getId());

      return entity.getId() == null
            ? save(entity, args)
            : update(entity, args);
   }

   private WriteOff update(WriteOff entity, Map<String, Object> args) {

      jdbcNamed.update("UPDATE Receipt SET supplier_id = :supplierId WHERE id = :id", args);
      jdbcNamed.update("DELETE FROM ReceiptItem WHERE receipt_id = :id", args);

      saveItems(entity.getId(), entity.getItems());

      return find(entity.getId())
            .orElseThrow(() -> new RuntimeException("Could not load updated object"));
   }

   private WriteOff save(WriteOff entity, Map<String, Object> args) {
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

   private void saveItems(long writeOffId, Collection<WriteOffItem> items) {

      @SuppressWarnings("unchecked")
      Map<String, ?>[] batchArgs = items.stream()
            .map(item -> ImmutableMap.builder()
                  .put("writeOffId", writeOffId)
                  .put("receiptItemId", item.getReceiptItem().getId())
                  .put("quantity", item.getQuantity())
//                  .put("productNameSnapshot", item.getProduct().getName())
//                  .put("productMeasureUnitSnapshot", item.getProduct().getMeasureUnit().name())
//                  .put("productUnitWeightSnapshot", item.getProduct().getUnitWeight())
                  .build())
            .toArray(Map[]::new);

      jdbcNamed.batchUpdate("" +
                  "INSERT INTO WriteOffItem (writeOff_id, receiptItem_id, quantity) " +
                  "VALUES (:writeOffId, :receiptItemId, :quantity)",
            batchArgs
      );
   }

   @Override
   public List<WriteOff> load() {
      return load(new WriteOffFilter(null, null, null));
   }

   @Override
   public List<WriteOff> load(WriteOffFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("periodStart", filter.getPeriodStart());
      args.put("periodEnd", filter.getPeriodEnd());

      return jdbcNamed.query("" +
                  "SELECT " +
                  "  wo.id, wo.periodStart, wo.periodEnd, wo.dateCreated, " +
                  "  woi.id, woi.quantity, " +
                  "  ri.id, ri.unitPrice, ri.quantity, " +
                  "  p.id, p.name, p.measureUnit, p.unitWeight, " +
                  "  pg.id, pg.name, pg.kcal100 " +

                  "FROM WriteOff wo " +
                  "  JOIN WriteOffItem woi ON woi.writeOff_id = wo.id " +
                  "  JOIN ReceiptItem ri ON ri.id = woi.receiptItem_id " +
                  "  JOIN Product p ON p.id = ri.product_id " +
                  "  JOIN ProductGroup pg ON pg.id = p.productGroup_id " +

                  "WHERE (:id IS NULL OR :id = wo.id) " +
                  "  AND (" +
                  "     :periodStart IS NULL " +
                  "     OR :periodStart <= wo.periodStart " +
                  "     OR :periodStart BETWEEN wo.periodStart AND wo.periodEnd" +
                  "  ) " +
                  "  AND (" +
                  "     :periodEnd IS NULL " +
                  "     OR :periodEnd >= wo.periodEnd " +
                  "     OR :periodEnd BETWEEN wo.periodStart AND wo.periodEnd" +
                  "  )",
            args,
            new WriteOffResultSetExtractor()
      );
   }

   @Override
   public Optional<WriteOff> find(Long id) {
      List<WriteOff> result = load(new WriteOffFilter(id, null, null));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM WriteOff WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<WriteOff> entityClass() {
      return WriteOff.class;
   }
}
