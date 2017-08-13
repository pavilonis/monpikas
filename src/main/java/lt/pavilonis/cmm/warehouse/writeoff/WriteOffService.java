package lt.pavilonis.cmm.warehouse.writeoff;

import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class WriteOffService {

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private ReceiptItemRepository receiptItemRepository;

   public WriteOff preview(LocalDate periodStart, LocalDate periodEnd) {

      Map<Long, BigDecimal> productGroupConsumption = findConsumption(periodStart, periodEnd);

      // Map<Product Group, Map<Receipt Item, Quantity>>
      Map<Long, Map<Long, BigDecimal>> availableByProductGroupAndReceiptItem = findAvailable();

      WriteOff result = new WriteOff(periodStart, periodEnd);
      Collection<WriteOffItem> writeOffItems = result.getItems();

      productGroupConsumption.forEach((productGroupId, consumption) -> {
         Map<Long, BigDecimal> receiptItemAvailability = availableByProductGroupAndReceiptItem.get(productGroupId);

         if (!CollectionUtils.isEmpty(receiptItemAvailability)) {
            Map<Long, BigDecimal> receiptItemUsageExtracted =
                  BigDecimalMapValueExtractor.extract(consumption, receiptItemAvailability);

            receiptItemUsageExtracted.forEach((receiptItemId, usageExtract) -> {
               ReceiptItem receiptItem = receiptItemRepository.load(receiptItemId);
               writeOffItems.add(new WriteOffItem(receiptItem, usageExtract));
            });
         }
      });
      return result;
   }

   // Map<Product Group, Map<Receipt Item, Quantity>>
   private Map<Long, Map<Long, BigDecimal>> findAvailable() {
      Map<Long, Map<Long, BigDecimal>> result = new HashMap<>();
      jdbc.query("" +
                  "SELECT " +
                  "  pGroup.id, " +
                  "  rItem.id, " +
                  "  rItem.quantity * IF(p.measureUnit = 'PIECE', 1, p.unitWeight) - SUM(COALESCE(woi.quantity, 0))" +
                  "FROM ReceiptItem rItem " +
                  "  JOIN Product p ON p.id = rItem.product_id " +
                  "  JOIN ProductGroup pGroup ON pGroup.id = p.productGroup_id " +
                  "  LEFT JOIN WriteOffItem woi ON woi.receiptItem_id = rItem.id " +
                  "  GROUP BY rItem.id",
            (rs) -> {
               result
                     .computeIfAbsent(rs.getLong(1), id -> new HashMap<>())
                     .put(rs.getLong(2), rs.getBigDecimal(3));
            }
      );
      return result;
   }

   protected Map<Long, BigDecimal> findConsumption(LocalDate periodStart, LocalDate periodEnd) {

      Map<Long, BigDecimal> productGroupConsumption = new HashMap<>();

      jdbc.query("" +
                  "SELECT pg.id, tcp.outputWeight " +
                  "FROM MenuRequirement mr " +
                  "  JOIN MenuRequirementTechCardSet mrtcs ON mrtcs.menuRequirement_id = mr.id " +
                  "  JOIN TechCardSet tcs ON tcs.id = mrtcs.techCardSet_id " +
                  "  JOIN TechCardSetTechCard tcstc ON tcstc.techCardSet_id = tcs.id " +
                  "  JOIN TechCard tc ON tc.id = tcstc.techCard_id " +
                  "  JOIN TechCardProduct tcp ON tcp.techCard_id = tc.id " +
                  "  JOIN ProductGroup pg ON pg.id = tcp.productGroup_id " +
                  "WHERE mr.date BETWEEN ? AND ?",

            rs -> {
               long productGroup = rs.getLong(1);
               BigDecimal weight = rs.getBigDecimal(2);

               BigDecimal updatedValue = productGroupConsumption
                     .getOrDefault(productGroup, BigDecimal.ZERO)
                     .add(weight);

               productGroupConsumption.put(productGroup, updatedValue);
            },

            periodStart, periodEnd
      );

      return productGroupConsumption;
   }
}
