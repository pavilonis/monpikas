package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.math.BigDecimal;

public final class TechnologicalCardItem extends Identified<Long> {

   private final TechnologicalCard dish;
   private final ProductGroup productGroup;
   private final BigDecimal outputWeight;

   public TechnologicalCardItem(long id, TechnologicalCard dish, ProductGroup productGroup, BigDecimal outputWeight) {
      setId(id);
      this.dish = dish;
      this.productGroup = productGroup;
      this.outputWeight = outputWeight;
   }

   public TechnologicalCard getDish() {
      return dish;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public BigDecimal getOutputWeight() {
      return outputWeight;
   }
}
