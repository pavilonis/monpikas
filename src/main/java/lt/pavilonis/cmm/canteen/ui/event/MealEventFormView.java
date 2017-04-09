package lt.pavilonis.cmm.canteen.ui.event;


import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.ui.user.UserMealFilter;
import lt.pavilonis.cmm.canteen.ui.user.UserMealGrid;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

final class MealEventFormView extends FieldLayout<MealEventLog> {

   private final UserMealGrid grid;
   private final UserMealService service;
   private final TextField textFilter = new ATextField(getClass(), "name");
   private final EnumComboBox<MealType> mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);
   private final ADateField date = new ADateField(getClass(), "date")
         .withValue(LocalDate.now())
         .withRequired();

   MealEventFormView(UserMealService service) {
      this.service = service;
      this.grid = new UserMealGrid();//"Pasirinkite mokinį");
      grid.setWidth(100, Unit.PERCENTAGE);
      grid.setHeight(340, Unit.PIXELS);

      List<String> columns = Arrays.asList("user.name", "user.group");
      grid.getColumns().stream()
            .filter(col -> !columns.contains(col.getId()))
            .forEach(col -> col.setHidden(true));

      addComponents(new HorizontalLayout(date, mealType), textFilter, grid);

      textFilter.addValueChangeListener(change -> updateTable(change.getValue(), mealType.getValue()));
      mealType.addValueChangeListener(change -> updateTable(textFilter.getValue(), mealType.getValue()));
      updateTable(null, mealType.getValue());
   }

   private void updateTable(String text, MealType value) {
      UserMealFilter filter = new UserMealFilter(value, text, true);
      List<UserMeal> beans = service.load(filter);

      grid.deselectAll();
      grid.setItems(beans);
   }

   Set<UserMeal> getGridSelection() {
      return grid.getSelectedItems();
   }
}
