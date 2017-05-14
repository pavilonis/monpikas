package lt.pavilonis.cmm.warehouse.techcard.field;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.math.BigDecimal;

public final class TechCardProductGroupOutput extends Identified<Void> {

   private final ProductGroup productGroup;
   private final BigDecimal outputWeight;

   public TechCardProductGroupOutput(ProductGroup productGroup, BigDecimal outputWeight) {
      this.productGroup = productGroup;
      this.outputWeight = outputWeight;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public BigDecimal getOutputWeight() {
      return outputWeight;
   }
}
