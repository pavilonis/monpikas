package lt.pavilonis.cmm.warehouse.techcardset;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetType;

import java.util.List;

import static lt.pavilonis.cmm.App.translate;

public class TechCardFields extends FieldLayout<TechCardSet> {

   private final TextField name = new TextField(translate(TechCardSet.class, "name"));
   private final ComboBox<TechCardSetType> type = new ComboBox<>(translate(TechCardSet.class, "type"));
   private final OneToManyField<TechCard> techCards = new OneToManyField<>(
         TechCard.class, "name", "group", "productGroupOutputWeight", "caloricity"
   );

   TechCardFields(List<TechCardSetType> techCardSetTypes) {
      type.setItems(techCardSetTypes);
      HorizontalLayout top = new HorizontalLayout(name, type);
      techCards.setTableWidth(676, Unit.PIXELS);
      addComponents(top, techCards);
   }
}
