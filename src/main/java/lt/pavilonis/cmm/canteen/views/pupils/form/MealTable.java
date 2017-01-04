package lt.pavilonis.cmm.canteen.views.pupils.form;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.views.converters.MealTypeCellConverter;
import lt.pavilonis.cmm.canteen.views.converters.ModifiedStringToDoubleConverter;
import org.vaadin.viritin.fields.MTable;

import java.util.List;

public class MealTable extends MTable<Meal> {
   public MealTable(List<Meal> meals) {

      addBeans(meals);

      setVisibleColumns("id", "name", "type", "price");
      setColumnHeaders("Id", "Pavadinimas", "Tipas", "Kaina");
      setColumnCollapsed("id", true);

      setConverter("type", new MealTypeCellConverter());
      setConverter("price", new ModifiedStringToDoubleConverter());

      setColumnCollapsingAllowed(true);
      setSelectable(true);
//      setMultiSelect(true);
//      setMultiSelectMode(MultiSelectMode.DEFAULT);
      setNullSelectionAllowed(false);
      setCacheRate(5);
      setHeight("370px");
   }
}
