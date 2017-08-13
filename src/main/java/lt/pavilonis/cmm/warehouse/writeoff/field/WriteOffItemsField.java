package lt.pavilonis.cmm.warehouse.writeoff.field;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.writeoff.WriteOffItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WriteOffItemsField extends OneToManyField<WriteOffItem> {

   private final Collection<Product> products = Collections.emptyList();
   private final Collection<ProductGroup> productGroups= Collections.emptyList();

   public WriteOffItemsField() {
      super(
            WriteOffItem.class,
//            ImmutableMap.of("productName", item -> item.getProduct().getName()),
            "productName", "quantity", "unitPrice", "cost"
      );
//      this.products = products;
//      this.productGroups = products.stream()
//            .map(Product::getProductGroup)
//            .collect(Collectors.toList());
   }

   @Override
   protected void actionAdd() {
      new WriteOffItemPopup(
            products,
            productGroups,
            createSelectionConsumer().andThen(items -> fireComponentEvent())
      );
   }
}
