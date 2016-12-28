package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.views.converters.MealTypeCellConverter;
import lt.pavilonis.monpikas.server.views.converters.PupilTypeCellConverter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.vaadin.ui.Table.Align.CENTER;

public class MealEventListView extends VerticalLayout {

   private final BeanContainer<Long, MealEventLog> container = new BeanContainer<>(MealEventLog.class);
   private final Table table = new MealEventTable(container);
   private final MealEventListFilterPanel filterPanel = new MealEventListFilterPanel();
   private final MealEventListControlPanel controlPanel = new MealEventListControlPanel();

   public MealEventListView() {
      setSizeFull();
      addComponents(filterPanel, table, controlPanel);
      setExpandRatio(table, 1f);
      filterPanel.addFilterButtonListener(filterButtonClicked -> {
         container.removeAllContainerFilters();
         container.addContainerFilter(filterPanel.getFilter());
      });
      filterPanel.addCancelFilterButtonListener(cancelFilterButtonClicked -> {
         filterPanel.cleanFields();
         container.removeAllContainerFilters();
      });
   }

//   public void setTableClickListener(ItemClickListener listener) {
//      table.addItemClickListener(listener);
//   }

   public BeanContainer<Long, MealEventLog> getContainer() {
      return container;
   }

//   public MealEventListFilterPanel getFilterPanel() {
//      return filterPanel;
//   }

   public MealEventListControlPanel getControlPanel() {
      return controlPanel;
   }

   public Table getTable() {
      return table;
   }

   private class MealEventTable extends Table {
      public MealEventTable(BeanContainer<Long, MealEventLog> container) {
         container.setBeanIdProperty("id");
         setSizeFull();
         setContainerDataSource(container);
         setConverter("date", new StringToDateConverter() {
            @Override
            public DateFormat getFormat(Locale locale) {
               return new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            }
         });
         setConverter("price", new StringToDoubleConverter() {
            @Override
            protected NumberFormat getFormat(Locale locale) {
               return new DecimalFormat("0.00");
            }
         });
         setConverter("mealType", new MealTypeCellConverter());
         setConverter("pupilType", new PupilTypeCellConverter());
         setVisibleColumns("id", "cardCode", "grade", "name", "date", "mealType", "pupilType", "price");
         setColumnHeaders("Id", "Kodas", "Klasė", "Vardas", "Data", "Maitinimo tipas", "Mokinio tipas", "Kaina");
         setColumnWidth("cardCode", 100);
         setColumnWidth("grade", 60);
         setColumnAlignment("grade", CENTER);
         setColumnWidth("birthDate", 130);
         setColumnCollapsingAllowed(true);
         setColumnCollapsed("id", true);
         setColumnCollapsed("cardCode", true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
      }
   }
}
