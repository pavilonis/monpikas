package lt.pavilonis.monpikas.server.views.settings;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.views.converters.MealTypeCellConverter;
import lt.pavilonis.monpikas.server.views.converters.ModifiedStringToDoubleConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MealListView extends VerticalLayout {

   private final Table table = new Table();
   private final BeanContainer<Long, Meal> container = new BeanContainer<>(Meal.class);
   private final TableControlPanel controlPanel = new TableControlPanel();

   //TODO add popup dialog "are you sure..."

   public MealListView() {
      setMargin(true);
      setSizeFull();
      container.setBeanIdProperty("id");
      table.setSizeFull();
      table.setContainerDataSource(container);
      container.sort(new Object[]{"id"}, new boolean[]{true});

      table.setVisibleColumns("id", "name", "type", "price");
      table.setColumnHeaders("Id", "Pavadinimas", "Tipas", "Kaina");

      table.setConverter("type", new MealTypeCellConverter());
      table.setConverter("price", new ModifiedStringToDoubleConverter());

      table.setColumnCollapsingAllowed(true);
      table.setColumnCollapsed("id", true);
      table.setSelectable(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);

      addComponents(new Label("Porcijos"), table, controlPanel);
      setExpandRatio(table, 1f);
   }

   public void setTableClickListener(ItemClickListener listener) {
      table.addItemClickListener(listener);
   }

   public BeanContainer<Long, Meal> getContainer() {
      return container;
   }

   public TableControlPanel getControlPanel() {
      return controlPanel;
   }

   public Table getTable() {
      return table;
   }
}
