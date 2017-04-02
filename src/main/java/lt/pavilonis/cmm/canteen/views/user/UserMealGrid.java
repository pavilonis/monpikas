package lt.pavilonis.cmm.canteen.views.user;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.converter.CollectionValueProviderAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserMealGrid extends ListGrid<UserMeal> {

   public UserMealGrid() {
      super(UserMeal.class);
   }

   @Override
   protected List<String> getProperties(Class<UserMeal> type) {
      return Arrays.asList("user.cardCode", "user.name", "user.birthDate",
            "user.group", "mealData.meals", "mealData.comment");
   }

   @Override
   protected Map<String, ValueProvider<UserMeal, ?>> getCustomColumns() {
      return ImmutableMap.<String, ValueProvider<UserMeal, ?>>builder()

            .put("user.cardCode", item -> item.getUser().getCardCode())
            .put("user.name", item -> item.getUser().getName())
            .put("user.birthDate", item -> item.getUser().getBirthDate())
            .put("user.group", item -> item.getUser().getGroup())
            .put("mealData.meals", new CollectionValueProviderAdapter<>(item -> item.getMealData().getMeals()))
            .put("mealData.comment", item -> item.getMealData().getComment())
            .build();
   }

   @Override
   protected void customize() {
      getColumn("user.group").setWidth(90);
      getColumn("user.group").setWidth(90);
      getColumn("user.birthDate").setWidth(130);
      getColumn("user.cardCode").setWidth(180);
      getColumn("user.name").setWidth(300);
      sort("user.name");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("user.cardCode");
   }
}
