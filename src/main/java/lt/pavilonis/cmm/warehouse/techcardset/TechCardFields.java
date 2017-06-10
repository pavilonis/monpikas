package lt.pavilonis.cmm.warehouse.techcardset;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.mealtype.MealType;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;

import java.util.List;

public class TechCardFields extends FieldLayout<TechCardSet> {

   private final TextField name = new TextField(App.translate(TechCardSet.class, "name"));
   private final OneToManyField<TechCard> techCards = new OneToManyField<>(TechCard.class);
   private final ComboBox<MealType> type = new ComboBox<>(App.translate(TechCardSet.class, "type"));

   TechCardFields(List<MealType> mealTypes) {
      type.setItems(mealTypes);
      addComponents(name, type, techCards);
   }
}
