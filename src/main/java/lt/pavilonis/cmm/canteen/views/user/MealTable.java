package lt.pavilonis.cmm.canteen.views.user;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;

import java.util.Collections;
import java.util.List;

public final class MealTable extends ListTable<Meal> {

   MealTable(MessageSourceAdapter messages, List<Meal> meals) {
      this(messages);
      addBeans(meals);
   }

   public MealTable(MessageSourceAdapter messages) {
      super(Meal.class);
      //TODO translate
      withProperties("id", "name", "type", "startTime", "endTime", "price");
      withColumnHeaders("Id", "Pavadinimas", "Tipas", "Nuo", "Iki", "Kaina");

      setConverter("type", new ToStringConverterAdapter<MealType>(MealType.class) {
         @Override
         protected String toPresentation(MealType model) {
            return messages.get(MealType.class, model.name());
         }
      });
      setConverter("price", new ModifiedStringToDoubleConverter());

      setSortContainerPropertyId("name");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("id");
   }
}
