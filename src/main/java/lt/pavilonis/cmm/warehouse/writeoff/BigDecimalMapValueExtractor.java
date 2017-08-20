package lt.pavilonis.cmm.warehouse.writeoff;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

final class BigDecimalMapValueExtractor {

   /**
    * @return Map of extracted amounts
    */
   static Map<Long, BigDecimal> extract(BigDecimal amountToExtract, Map<Long, BigDecimal> mapToExtractFrom) {

      BigDecimal weightCollected = BigDecimal.ZERO;
      Map<Long, BigDecimal> result = new HashMap<>();

      BigDecimal availableWeightSum = mapToExtractFrom.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

      for (Map.Entry<Long, BigDecimal> entry : mapToExtractFrom.entrySet()) {

         long receiptItemId = entry.getKey();
         BigDecimal weight = entry.getValue();

         if (weightCollected.compareTo(amountToExtract) == 0 || availableWeightSum.compareTo(weightCollected) == 0)
            return result;

         // needed is LESS than receipt item available weight
         if (amountToExtract.compareTo(weight) < 0) {
            result.put(receiptItemId, amountToExtract);
            mapToExtractFrom.put(receiptItemId, weight.subtract(amountToExtract));
            return result;

         } else {
            // When needed weight is same or bigger than offered - taking it all
            result.put(receiptItemId, weight);
            mapToExtractFrom.put(receiptItemId, BigDecimal.ZERO);
            amountToExtract = amountToExtract.subtract(weight);
         }
      }

      return result;
   }
}
