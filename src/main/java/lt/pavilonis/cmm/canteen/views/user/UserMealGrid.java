package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.converter.CollectionValueProviderAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserMealGrid extends ListGrid<UserMeal> {

   public UserMealGrid() {
      super(UserMeal.class);
   }

   @Override
   protected void addCustomColumns() {
      addColumn(new CollectionValueProviderAdapter<>(item -> item.getMealData().getMeals()))
            .setId("mealData.meals");
   }

   @Override
   protected void customize() {
      getColumn("user.group").setWidth(90);
      getColumn("user.group").setWidth(90);
      getColumn("user.birthDate").setWidth(130);
      getColumn("user.cardCode").setWidth(180);
      getColumn("user.name").setWidth(300);

      sort("user.name");


//      setSortableProperties("user.cardCode", "user.firstName", "user.lastName",
//            "user.birthDate", "user.group", "mealData.comment");
   }

   @Override
   protected List<String> getProperties(Class<UserMeal> type) {
      return Arrays.asList("user.cardCode", "user.name",
            "user.birthDate", "user.group", "mealData.meals", "mealData.comment");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("user.cardCode");
   }
}
