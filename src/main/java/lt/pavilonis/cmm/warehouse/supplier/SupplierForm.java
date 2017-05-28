package lt.pavilonis.cmm.warehouse.supplier;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;

public final class SupplierForm extends FieldLayout<Supplier> {

   private final TextField name = new ATextField(Supplier.class, "name");
   private final TextField code = new ATextField(Supplier.class, "code");

   public SupplierForm() {
      addComponents(name, code);
   }
}
