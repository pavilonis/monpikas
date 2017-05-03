package lt.pavilonis.cmm.warehouse.techcardgroup;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

public final class TechnologicalCardGroupForm extends FieldLayout<TechnologicalCardGroup> {
   private final TextField name = new ATextField(Supplier.class, "name");

   public TechnologicalCardGroupForm() {
      addComponents(name);
   }
}
