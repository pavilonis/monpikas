package lt.pavilonis.cmm.warehouse.receipt;

import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.receipt.field.ReceiptItemsField;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.util.List;

public class ReceiptFields extends FieldLayout<Receipt> {

   private final ComboBox<Supplier> supplier = new ComboBox<>(App.translate(Receipt.class, "supplier"));
   private final OneToManyField<ReceiptItem> items;

   ReceiptFields(List<Supplier> techCardSetTypes, List<Product> products) {
      this.items = new ReceiptItemsField(products);
      supplier.setItems(techCardSetTypes);
      supplier.setItemCaptionGenerator(Supplier::getName);
      supplier.setWidth(300, Unit.PIXELS);

      addComponents(supplier, items);
   }
}
