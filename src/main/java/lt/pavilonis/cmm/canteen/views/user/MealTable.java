package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MealTable extends ListTable<Meal> {

   public MealTable(List<Meal> meals) {
      super(Meal.class);
      addBeans(meals);
   }

   @Override
   protected void customize(MessageSourceAdapter messageSource) {
      setConverter("type", new ToStringConverterAdapter<MealType>(MealType.class) {
         @Override
         protected String toPresentation(MealType model) {
            return messageSource.get(MealType.class, model.name());
         }
      });

      setConverter("price", new ModifiedStringToDoubleConverter());

      setSortContainerPropertyId("name");
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("id", "name", "type", "startTime", "endTime", "price");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("id");
   }
}
