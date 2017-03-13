package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.common.ListGrid;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class MealGrid extends ListGrid<Meal> {

   private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.00");

   public MealGrid(List<Meal> meals) {
      super(Meal.class);
      setItems(meals);
   }

   @Override
   protected void addCustomColumns() {
      addColumn(meal -> messageSource.get(meal.getType().getClass(), meal.getType().name()))
            .setId("type");
      addColumn(meal -> NUMBER_FORMAT.format(meal.getPrice()))
            .setId("price");
   }

   @Override
   protected List<String> getProperties(Class<Meal> type) {
      return Arrays.asList("id", "name", "type", "startTime", "endTime", "price");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("id");
   }
}
