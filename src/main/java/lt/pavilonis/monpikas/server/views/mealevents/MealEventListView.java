package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.views.converters.PortionTypeCellConverter;
import lt.pavilonis.monpikas.server.views.converters.SimpleStringToLongConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MealEventListView extends VerticalLayout {

   private Table table = new Table();
   BeanContainer<Long, MealEvent> container = new BeanContainer<>(MealEvent.class);
   MealEventListFilterPanel filterPanel = new MealEventListFilterPanel();
   MealEventListControlPanel controlPanel = new MealEventListControlPanel();

   public MealEventListView() {
      setSizeFull();
      container.setBeanIdProperty("id");
      table.setSizeFull();
      table.setContainerDataSource(container);
      container.sort(new Object[]{"date"}, new boolean[]{true});
      table.setColumnHeader("id", "ID");
      table.setColumnHeader("name", "Vardas");
      table.setColumnHeader("cardId", "Kortelės #");
      table.setColumnHeader("date", "Data");
      table.setColumnHeader("type", "Tipas");
      table.setColumnHeader("price", "Kaina (Lt.)");
      table.setConverter("date", new StringToDateConverter() {
         @Override
         public DateFormat getFormat(Locale locale) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
         }
      });
      table.setConverter("type", new PortionTypeCellConverter());
      table.setConverter("cardId", new SimpleStringToLongConverter());
      table.setVisibleColumns(new String[]{"id", "cardId", "name", "date", "type", "price"});
      table.setColumnWidth("cardId", 100);
      table.setColumnWidth("birthDate", 130);
      table.setColumnCollapsingAllowed(true);
      table.setColumnCollapsed("id", true);
      table.setSelectable(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);

      addComponents(filterPanel, table, controlPanel);
      setExpandRatio(table, 1f);
      filterPanel.addFilterButtonListener(filterButtonClicked -> {
               container.removeAllContainerFilters();
               container.addContainerFilter(filterPanel.getFilter());
            }
      );
      filterPanel.addCancelFilterButtonListener(cancelFilterButtonClicked -> {
         filterPanel.cleanFields();
         container.removeAllContainerFilters();
      });
   }

   public void setTableClickListener(ItemClickListener listener) {
      table.addItemClickListener(listener);
   }

   public BeanContainer<Long, MealEvent> getContainer() {
      return container;
   }

   public MealEventListFilterPanel getFilterPanel() {
      return filterPanel;
   }

   public MealEventListControlPanel getControlPanel() {
      return controlPanel;
   }

   public Table getTable() {
      return table;
   }
}
