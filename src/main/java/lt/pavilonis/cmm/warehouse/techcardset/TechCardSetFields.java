package lt.pavilonis.cmm.warehouse.techcardset;

import com.google.common.collect.ImmutableMap;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetType;

import java.util.List;
import java.util.stream.Collectors;

import static lt.pavilonis.cmm.App.translate;

public class TechCardSetFields extends FieldLayout<TechCardSet> {

   private final TextField name = new TextField(translate(TechCardSet.class, "name"));
   private final ComboBox<TechCardSetType> type = new ComboBox<>(translate(TechCardSet.class, "type"));
   private final OneToManyField<TechCard> techCards = new OneToManyField<>(
         TechCard.class,
         ImmutableMap.of("productGroupOutputWeight", item -> item.getProductGroupOutputWeight()
                     .entrySet()
                     .stream()
                     .map(entry -> entry.getKey().getName() + ":" + entry.getValue() + "g")
                     .collect(Collectors.joining(", "))
         ),
         "name", "group", "caloricity", "productGroupOutputWeight"
   );

   TechCardSetFields(List<TechCardSetType> techCardSetTypes) {
      type.setItems(techCardSetTypes);
      type.setItemCaptionGenerator(TechCardSetType::getName);
      type.setWidth(300, Unit.PIXELS);
      name.setWidth(300, Unit.PIXELS);
      HorizontalLayout top = new HorizontalLayout(name, type);
      techCards.setTableHeight(336, Unit.PIXELS);
      addComponents(top, techCards);
   }
}
