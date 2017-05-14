package lt.pavilonis.cmm.warehouse.techcard.field;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.util.List;

public class TechCardProductGroupOutputField extends OneToManyField<TechCardProductGroupOutput> {

   private final List<ProductGroup> productGroups;

   public TechCardProductGroupOutputField(List<ProductGroup> productGroups) {
      super(
            TechCardProductGroupOutput.class,
            ImmutableMap.of("productGroup", item -> item.getProductGroup().getName()),
            "productGroup", "outputWeight"
      );
      this.productGroups = productGroups;
   }

   @Override
   protected void actionAdd() {
      new TechCardProductGroupOutputPopup(productGroups, createSelectionConsumer());
   }
}
