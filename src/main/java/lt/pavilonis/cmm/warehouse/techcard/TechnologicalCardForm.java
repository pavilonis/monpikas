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

   @PropertyId("technologicalCardGroup")
   private final ComboBox<TechnologicalCardGroup> technologicalCardGroup;

   public TechnologicalCardForm(List<TechnologicalCardGroup> dishGroups) {
      technologicalCardGroup = new ComboBox<>(App.translate(TechnologicalCardGroup.class, "technologicalCardGroup"), dishGroups);
      technologicalCardGroup.setItemCaptionGenerator(Named::getName);
      addComponents(name, technologicalCardGroup);
   }

   @Override
   public void manualBinding(Binder<TechnologicalCard> binding) {
      binding.bind(technologicalCardGroup,
            TechnologicalCard::getTechnologicalCardGroup,
            TechnologicalCard::setTechnologicalCardGroup
      );
   }
}
