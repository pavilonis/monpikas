package lt.pavilonis.cmm.warehouse.dishItem;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.dish.Dish;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.math.BigDecimal;

public class DishItem extends Identified<Long> {

   private Dish dish;
   private ProductGroup productGroup;
   private BigDecimal outputWeight;

   public DishItem(long id, Dish dish, ProductGroup productGroup, BigDecimal outputWeight) {
      setId(id);
      this.dish = dish;
      this.productGroup = productGroup;
      this.outputWeight = outputWeight;
   }

   public DishItem() {
   }

   public Dish getDish() {
      return dish;
   }

   public void setDish(Dish dish) {
      this.dish = dish;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public void setProductGroup(ProductGroup productGroup) {
      this.productGroup = productGroup;
   }

   public BigDecimal getOutputWeight() {
      return outputWeight;
   }

   public void setOutputWeight(BigDecimal outputWeight) {
      this.outputWeight = outputWeight;
   }
}
