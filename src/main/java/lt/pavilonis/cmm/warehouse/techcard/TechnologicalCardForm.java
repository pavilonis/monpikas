package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroup;

import java.util.List;

public final class TechnologicalCardForm extends FieldLayout<TechnologicalCard> {

   private final TextField name = new ATextField(TechnologicalCard.class, "name");

   @PropertyId("dishGroup")
   private final ComboBox<TechnologicalCardGroup> dishGroup;

   public TechnologicalCardForm(List<TechnologicalCardGroup> dishGroups) {
      dishGroup = new ComboBox<>(App.translate(TechnologicalCardGroup.class, "dishGroup"), dishGroups);
      dishGroup.setItemCaptionGenerator(Named::getName);
      addComponents(name, dishGroup);
   }

   @Override
   public void manualBinding(Binder<TechnologicalCard> binding) {
      binding.bind(dishGroup, TechnologicalCard::getDishGroup, TechnologicalCard::setDishGroup);
   }
}
