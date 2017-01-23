package lt.pavilonis.cmm.canteen.views.event;


import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.canteen.views.user.UserMealFilter;
import lt.pavilonis.cmm.common.FormView;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Date;
import java.util.List;

final class MealEventFormView extends FormView<MealEventLog> {

   private final UserMealTable table;
   private final MDateField date = new MDateField("Pasirinkite data: ", new Date())
         .withRequired(true);
   private final ComboBox mealType = new EnumComboBox<>(MealType.class)
         .withRequired(true);

   MealEventFormView(UserMealService userMeals, MessageSourceAdapter messages) {

      table = new UserMealTable("Pasirinkite mokinį", userMeals.loadAll(new UserMealFilter(null, null, true)));

      MTextField textFilter = new MTextField(messages.get(this, "name")).withTextChangeListener(
            change -> {
               table.select(null);
               table.setBeans(userMeals.loadAll(new UserMealFilter(null, change.getText(), true)));
            }
      );

      date.setDateFormat("yyyy-MM-dd");

      add(
            new MHorizontalLayout(date, mealType)
                  .withMargin(false),
            textFilter,
            table
      );
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
         setPageLength(7);
         setWidth("100%");
//         withSize(MSize.size("360px", "290px"));
         setSortContainerPropertyId("user.name");
         sort();
      }
   }
}
