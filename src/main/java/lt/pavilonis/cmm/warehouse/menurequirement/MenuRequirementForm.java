package lt.pavilonis.cmm.warehouse.menurequirement;

import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.mealtype.MealType;

public final class MenuRequirementForm extends FieldLayout<MenuRequirement> {

   private final TextField name = new ATextField(MealType.class, "name");
   private final DateField date = new ADateField(MenuRequirement.class, "date");

   public MenuRequirementForm() {
      addComponents(name, date);
   }
}
