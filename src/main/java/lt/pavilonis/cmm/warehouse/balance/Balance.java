package lt.pavilonis.cmm.warehouse.balance;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.math.BigDecimal;
import java.util.Map;

public final class Balance extends Identified<Void> {

   private ProductGroup productGroup;
   private Map<Product, BigDecimal> quantity;

   public Balance() {
   }

   public Balance(ProductGroup productGroup, Map<Product, BigDecimal> quantity) {
      this.productGroup = productGroup;
      this.quantity = quantity;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   public void setProductGroup(ProductGroup productGroup) {
      this.productGroup = productGroup;
   }

   public Map<Product, BigDecimal> getQuantity() {
      return quantity;
   }

   public void setQuantity(Map<Product, BigDecimal> quantity) {
      this.quantity = quantity;
   }
}
