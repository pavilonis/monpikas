package lt.pavilonis.cmm.warehouse.dish;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.dishGroup.DishGroup;

import java.util.List;

public class DishForm extends FieldLayout<Dish> {

   private final TextField name = new ATextField(Dish.class, "name");

   @PropertyId("dishGroup")
   private final ComboBox<DishGroup> dishGroup;

   public DishForm(List<DishGroup> dishGroups) {
      dishGroup = new ComboBox<>(App.translate(DishGroup.class, "dishGroup"), dishGroups);
      dishGroup.setItemCaptionGenerator(Named::getName);
      addComponents(name, dishGroup);
   }

   @Override
   public void manualBinding(Binder<Dish> binding) {
      binding.bind(dishGroup, Dish::getDishGroup, Dish::setDishGroup);
   }
}
