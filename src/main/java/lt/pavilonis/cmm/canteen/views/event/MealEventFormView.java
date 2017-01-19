package lt.pavilonis.cmm.canteen.views.event;


import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.common.FormView;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Date;
import java.util.List;

final class MealEventFormView extends FormView<MealEventLog> {

   private final UserMealTable table;
   private final MDateField date = new MDateField("Pasirinkite data: ", new Date())
         .withRequired(true);
   private final ComboBox mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);

   MealEventFormView(List<UserMeal> userMeals) {
      table = new UserMealTable("Pasirinkite mokinį", userMeals);
      date.setDateFormat("yyyy-MM-dd");

      add(new MHorizontalLayout(date, mealType).withMargin(false), table);
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
