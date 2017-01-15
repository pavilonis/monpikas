package lt.pavilonis.cmm.canteen.views.user.form;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.views.converter.MealTypeCellConverter;
import lt.pavilonis.cmm.canteen.views.converter.ModifiedStringToDoubleConverter;
import org.vaadin.viritin.fields.MTable;

import java.util.List;

public class MealTable extends MTable<Meal> {

   public MealTable() {
      setVisibleColumns("id", "name", "type", "startTime", "endTime", "price");
      setColumnHeaders("Id", "Pavadinimas", "Tipas", "Nuo", "Iki", "Kaina");
      setColumnCollapsingAllowed(true);
      setColumnCollapsed("id", true);

      setConverter("type", new MealTypeCellConverter());
      setConverter("price", new ModifiedStringToDoubleConverter());

      setSelectable(true);
//      setMultiSelect(true);
//      setMultiSelectMode(MultiSelectMode.DEFAULT);
      setNullSelectionAllowed(false);
      setCacheRate(5);
      setHeight("370px");
   }

   public MealTable(List<Meal> meals) {
      this();
      addBeans(meals);
   }
}
