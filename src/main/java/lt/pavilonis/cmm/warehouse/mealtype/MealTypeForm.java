package lt.pavilonis.cmm.warehouse.mealtype;

import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;

public final class MealTypeForm extends FieldLayout<MealType> {

   private final TextField name = new ATextField(MealType.class, "name");

   public MealTypeForm() {
      addComponents(name);
   }
}
