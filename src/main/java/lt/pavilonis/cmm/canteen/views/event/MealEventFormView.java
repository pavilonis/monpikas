package lt.pavilonis.cmm.canteen.views.event;


import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.user.UserMealFilter;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Date;
import java.util.List;

final class MealEventFormView extends FormView<MealEventLog> {

   private final UserMealTable table;
   private final UserMealService service;
   private final TextField textFilter = new ATextField(getClass(), "name");
   private final EnumComboBox<MealType> mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);
   private final MDateField date = new ADateField(getClass(), "date")
         .withValue(new Date())
         .withRequired(true);

   MealEventFormView(UserMealService service) {
      this.service = service;
      this.table = new UserMealTable("Pasirinkite mokinį");

      MHorizontalLayout top = new MHorizontalLayout(date, mealType)
            .withMargin(false);
      add(top, textFilter, table);

      textFilter.addTextChangeListener(change -> updateTable(change.getText(), mealType.getValue()));
      mealType.addValueChangeListener(change -> updateTable(textFilter.getValue(), mealType.getValue()));
      updateTable(null, mealType.getValue());
   }

   private void updateTable(String text, MealType value) {
      UserMealFilter filter = new UserMealFilter(value, text, true);
      List<UserMeal> beans = service.loadAll(filter);

      table.select(null);
      table.setBeans(beans);
   }

   UserMeal getTableValue() {
      return table.getValue();
   }

   private final class UserMealTable extends MTable<UserMeal> {
      private UserMealTable(String caption) {
         setCaption(caption);
         withProperties("user.name", "user.group");
         withColumnHeaders("Vardas", "Klasė");
         setColumnWidth("user.group", 85);
         setColumnCollapsingAllowed(true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
         setPageLength(7);
         setWidth("100%");
         setSortContainerPropertyId("user.name");
         sort();
      }
   }
}
