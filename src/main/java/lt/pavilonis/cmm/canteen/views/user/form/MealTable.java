package lt.pavilonis.cmm.canteen.views.user.form;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;

import java.util.Collections;
import java.util.List;

public class MealTable extends ListTable<Meal> {

   public MealTable(MessageSourceAdapter messages) {
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

      setHeight("370px");
      setSortContainerPropertyId("name");
   }

   public MealTable(MessageSourceAdapter messages, List<Meal> meals) {
      this(messages);
      addBeans(meals);
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("id");
   }
}
