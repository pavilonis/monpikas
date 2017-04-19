package lt.pavilonis.cmm.canteen.ui.event;


import com.vaadin.data.Binder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.Meal;
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
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

final class MealEventFormView extends FieldLayout<MealEventLog> {

   private final UserMealGrid grid;
   private final UserMealService service;
   private final TextField textFilter = new ATextField(getClass(), "name");
   private final EnumComboBox<MealType> mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);
   private final ADateField dateField = new ADateField(getClass(), "date")
         .withValue(LocalDate.now())
         .withRequired();

   MealEventFormView(UserMealService service) {
      this.service = service;
      this.grid = new UserMealGrid();
      grid.setWidth(100, Unit.PERCENTAGE);
      grid.setHeight(340, Unit.PIXELS);

      List<String> columns = Arrays.asList("user.name", "user.group");
      grid.getColumns().stream()
            .filter(col -> !columns.contains(col.getId()))
            .forEach(col -> col.setHidden(true));

      addComponents(new HorizontalLayout(dateField, mealType), textFilter, grid);

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

   @Override
   public void manualBinding(Binder<MealEventLog> binding) {
      binding.bind(
            dateField,
            mealEventLog -> {
               Instant instant = mealEventLog.getDate().toInstant();
               return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
            },
            (mealEventLog, localDate) -> {
               ZonedDateTime zonedDateTime = localDate.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault());
               mealEventLog.setDate(Date.from(zonedDateTime.toInstant()));

               //TODO use oneToMany field to get value like from other fields
               setSelectedUserData(mealEventLog);
            });
   }

   private void setSelectedUserData(MealEventLog model) {
      Set<UserMeal> value = grid.getSelectedItems();

      if (CollectionUtils.isNotEmpty(value)
            && value.size() == 1
            && mealType.getValue() != null) {

         UserMeal selectedUserMeal = value.iterator().next();

         Meal meal = selectedUserMeal.getMealData()
               .getMeals()
               .stream()
               .filter(portion -> portion.getType() == mealType.getValue())
               .findFirst()
               .orElseThrow(() -> new RuntimeException("Could not find required meal type. Should not happen"));

         model.setDate(correctMealEventTime(model.getDate(), meal));
         model.setName(selectedUserMeal.getUser().getName());
         model.setCardCode(selectedUserMeal.getUser().getCardCode());
         model.setGrade(selectedUserMeal.getUser().getGroup());
         model.setPupilType(selectedUserMeal.getMealData().getType());
         model.setPrice(meal.getPrice());
      }
   }

   private Date correctMealEventTime(Date date, Meal meal) {
      return org.joda.time.LocalDateTime.fromDateFields(date)
            .withTime(0, 0, 0, 0)
            .plusSeconds(meal.getStartTime().toSecondOfDay())
            .toDate();
   }
}
