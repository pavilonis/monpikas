package lt.pavilonis.cmm.warehouse.dishGroup;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

public final class DishGroupForm extends FieldLayout<DishGroup> {
   private final TextField name = new ATextField(Supplier.class, "name");

   public DishGroupForm() {
      addComponents(name);
   }
}
