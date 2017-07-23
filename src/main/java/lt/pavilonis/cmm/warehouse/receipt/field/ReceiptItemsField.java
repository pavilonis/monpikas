package lt.pavilonis.cmm.warehouse.receipt.field;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;
import lt.pavilonis.cmm.warehouse.techcard.field.TechCardProductGroupOutput;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptItemsField extends OneToManyField<ReceiptItem> {

   private final Collection<Product> products;
   private final Collection<ProductGroup> productGroups;

   public ReceiptItemsField(List<Product> products) {
      super(
            ReceiptItem.class,
            ImmutableMap.of("productName", item -> item.getProduct().getName()),
            "productName", "quantity", "unitPrice", "cost"
      );
      this.products = products;
      this.productGroups = products.stream()
            .map(Product::getProductGroup)
            .collect(Collectors.toList());
   }

   @Override
   protected void actionAdd() {
      new ReceiptItemPopup(
            products,
            productGroups,
            createSelectionConsumer().andThen(items -> fireComponentEvent())
      );
   }
}
