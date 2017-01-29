package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.CollectionCellConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserMealTable extends ListTable<UserMeal> {

   public UserMealTable() {
      super(UserMeal.class);
   }

   @Override
   protected void customize(MessageSourceAdapter messageSource) {
      setColumnWidth("user.group", 90);
      setColumnWidth("user.birthDate", 130);
      setColumnWidth("user.cardCode", 180);
      setColumnWidth("user.name", 300);

      setSortContainerPropertyId("user.name");

      setConverter("mealData.meals", new CollectionCellConverter());

      setSortableProperties("user.cardCode", "user.firstName", "user.lastName",
            "user.birthDate", "user.group", "mealData.comment");
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
