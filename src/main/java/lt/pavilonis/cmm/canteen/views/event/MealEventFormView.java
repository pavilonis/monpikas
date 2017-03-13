package lt.pavilonis.cmm.canteen.views.event;


import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.user.UserMealFilter;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

final class MealEventFormView extends FormView<MealEventLog> {

   private final UserMealGrid table;
   private final UserMealService service;
   private final TextField textFilter = new ATextField(getClass(), "name");
   private final EnumComboBox<MealType> mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);
   private final ADateField date = new ADateField(getClass(), "date")
         .withValue(LocalDate.now())
         .withRequired();

   MealEventFormView(UserMealService service) {
      this.service = service;
      this.table = new UserMealGrid("Pasirinkite mokinį");

      addComponents(new HorizontalLayout(date, mealType), textFilter, table);

      textFilter.addValueChangeListener(change -> updateTable(change.getValue(), mealType.getValue()));
      mealType.addValueChangeListener(change -> updateTable(textFilter.getValue(), mealType.getValue()));
      updateTable(null, mealType.getValue());
   }

   private void updateTable(String text, MealType value) {
      UserMealFilter filter = new UserMealFilter(value, text, true);
      List<UserMeal> beans = service.loadAll(filter);

      table.deselectAll();
      table.setItems(beans);
   }

   Set<UserMeal> getGridSelection() {
      return table.getSelectedItems();
   }

   private final class UserMealGrid extends ListGrid<UserMeal> {
      private UserMealGrid(String caption) {
         super(UserMeal.class);
         setCaption(caption);
         setHeaders("Vardas", "Klasė");
         getColumn("user.group").setWidth(85);
         setSelectionMode(SelectionMode.SINGLE);
         setWidth(100, Unit.PERCENTAGE);
         sort("user.name");
      }

      @Override
      protected List<String> getProperties(Class<UserMeal> type) {
         return Arrays.asList("user.name", "user.group");
      }
   }
}
