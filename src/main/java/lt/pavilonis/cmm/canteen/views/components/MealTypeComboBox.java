package lt.pavilonis.cmm.canteen.views.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.canteen.domain.MealType;

import static java.util.Arrays.asList;
import static lt.pavilonis.cmm.util.Messages.label;

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
