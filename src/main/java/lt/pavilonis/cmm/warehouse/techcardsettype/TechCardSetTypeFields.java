package lt.pavilonis.cmm.warehouse.techcardsettype;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;

public final class TechCardSetTypeFields extends FieldLayout<TechCardSetType> {

   private final TextField name = new ATextField(TechCardSetType.class, "name");

   public TechCardSetTypeFields() {
      addComponents(name);
   }
}
