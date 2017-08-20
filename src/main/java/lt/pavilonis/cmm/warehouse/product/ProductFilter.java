package lt.pavilonis.cmm.warehouse.product;

import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

public class ProductFilter extends IdTextFilter {

   private final ProductGroup productGroup;

   public ProductFilter(Long id) {
      super(id);
      this.productGroup = null;
   }

   public ProductFilter(String text, ProductGroup productGroup) {
      super(null, text);
      this.productGroup = productGroup;
   }

   public ProductGroup getProductGroup() {
      return productGroup;
   }

   @Override
   public ProductFilter withOffset(int value) {
      super.withOffset(value);
      return this;
   }

   @Override
   public ProductFilter withLimit(int value) {
      super.withLimit(value);
      return this;
   }
}
