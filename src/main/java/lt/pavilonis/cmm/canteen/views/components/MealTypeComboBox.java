package lt.pavilonis.monpikas.server.views.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.monpikas.server.domain.MealType;

import static java.util.Arrays.asList;
import static lt.pavilonis.monpikas.server.utils.Messages.label;

public class MealTypeComboBox extends ComboBox {

   public MealTypeComboBox() {
      setContainerDataSource(new BeanItemContainer<>(MealType.class, asList(MealType.values())));
      setValue(MealType.DINNER);
      setCaption(label("MealTypeComboBox.caption"));
   }

   @Override
   public String getItemCaption(Object itemId) {
      return label("MealType." + itemId);
   }

   @Override
   public boolean isNullSelectionAllowed() {
      return false;
   }

}
