package lt.pavilonis.cmm.canteen.views.event;


import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MGridLayout;

import java.util.Date;
import java.util.List;

final class MealEventFormView extends MGridLayout {

   private final UserMealTable table;
   private final MDateField date = new MDateField("Pasirinkite data: ", new Date());
   private final ComboBox mealType = new EnumComboBox<>(MealType.class);

   MealEventFormView(List<UserMeal> userMeals) {
      super(2, 2);
      this.table = new UserMealTable("Pasirinkite mokinį", userMeals);
      date.setDateFormat("yyyy-MM-dd");
      add(date, mealType);
      addComponent(table, 0, 1, 1, 1);
      setMargin(false);
      date.setRequired(true);
      mealType.setRequired(true);
   }

   UserMeal getTableValue() {
      return table.getValue();
   }

   private final class UserMealTable extends MTable<UserMeal> {
      private UserMealTable(String caption, List<UserMeal> userMeals) {
         super(userMeals);
         setCaption(caption);
         withProperties("user.name", "user.group");
         withColumnHeaders("Vardas", "Klasė");
         setColumnWidth("user.group", 85);
         setColumnCollapsingAllowed(true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
         setHeight(370, Unit.PIXELS);
         setWidth(100, Unit.PERCENTAGE);
      }
   }
}
