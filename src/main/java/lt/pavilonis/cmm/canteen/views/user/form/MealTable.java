package lt.pavilonis.cmm.canteen.views.user.form;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.converter.ModifiedStringToDoubleConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;
import org.vaadin.viritin.fields.MTable;

import java.util.List;

public class MealTable extends MTable<Meal> {

   public MealTable(MessageSourceAdapter messages) {
      withProperties("id", "name", "type", "startTime", "endTime", "price");
      withColumnHeaders("Id", "Pavadinimas", "Tipas", "Nuo", "Iki", "Kaina");
      setColumnCollapsingAllowed(true);
      setColumnCollapsed("id", true);

      setConverter("type", new ToStringConverterAdapter<MealType>(MealType.class) {
         @Override
         protected String toPresentation(MealType model) {
            return messages.get(MealType.class, model.name());
         }
      });
      setConverter("price", new ModifiedStringToDoubleConverter());

      setSelectable(true);
      setNullSelectionAllowed(false);
      setCacheRate(5);
      setHeight("370px");
   }

   public MealTable(MessageSourceAdapter messages, List<Meal> meals) {
      this(messages);
      addBeans(meals);
   }
}
