package lt.pavilonis.cmm.warehouse.dishItem;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.dish.Dish;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.math.BigDecimal;

public final class DishItem extends Identified<Long> {

   private final Dish dish;
   private final ProductGroup productGroup;
   private final BigDecimal outputWeight;

   public DishItem(long id, Dish dish, ProductGroup productGroup, BigDecimal outputWeight) {
      setId(id);
      this.dish = dish;
      this.productGroup = productGroup;
      this.outputWeight = outputWeight;
   }

   public Dish getDish() {
      return dish;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public BigDecimal getOutputWeight() {
      return outputWeight;
   }
}
