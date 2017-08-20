package lt.pavilonis.cmm.warehouse.receipt.field;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.receipt.ReceiptItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReceiptItemsField extends OneToManyField<ReceiptItem> {

   private final Collection<Product> products;
   private final Collection<ProductGroup> productGroups;

   public ReceiptItemsField(List<Product> products) {
      super(
            ReceiptItem.class,
            ImmutableMap.of(
                  "productName", item -> item.getProduct().getName(),
                  "unitWeight", item -> item.getProduct().getUnitWeight(),
                  "measureUnit", item -> App.translate(MeasureUnit.class, item.getProduct().getMeasureUnit().name()),
                  "receiptItemWeight", item -> item.getQuantity()
                        .multiply(new BigDecimal(item.getProduct().getUnitWeight()))
            ),
            "productName", "measureUnit", "quantity", "unitWeight", "receiptItemWeight", "unitPrice", "cost"
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
