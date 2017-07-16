package lt.pavilonis.cmm.warehouse.menurequirement;

import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetType;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;

import java.util.List;

public final class MenuRequirementForm extends FieldLayout<MenuRequirement> {

   private final OneToManyField<TechCardSet> techCardSets = new OneToManyField<>(TechCardSet.class);
   private final DateField date = new ADateField(MenuRequirement.class, "date");

   public MenuRequirementForm(List<TechCardSetType> types) {

      addComponents(date, techCardSets);
   }
}
