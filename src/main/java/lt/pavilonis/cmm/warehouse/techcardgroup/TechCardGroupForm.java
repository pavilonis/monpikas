package lt.pavilonis.cmm.warehouse.techcardgroup;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

public final class TechCardGroupForm extends FieldLayout<TechCardGroup> {
   private final TextField name = new ATextField(Supplier.class, "name");

   public TechCardGroupForm() {
      addComponents(name);
   }
}
