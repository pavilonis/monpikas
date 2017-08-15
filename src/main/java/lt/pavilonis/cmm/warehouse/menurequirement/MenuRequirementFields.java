package lt.pavilonis.cmm.warehouse.menurequirement;

import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;

public final class MenuRequirementFields extends FieldLayout<MenuRequirement> {

   private final OneToManyField<TechCardSet> techCardSets = new OneToManyField<>(
         TechCardSet.class, "name", "type", "caloricity", "techCards"
   );
   private final DateField date = new ADateField(MenuRequirement.class, "date");

   public MenuRequirementFields() {
      addComponents(date, techCardSets);
   }
}
