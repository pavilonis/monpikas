package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.MealEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MealEventListView extends VerticalLayout {

   private Table t = new Table();
   BeanContainer<Long, MealEvent> container = new BeanContainer<>(MealEvent.class);
   MealEventListFilterPanel filterPanel = new MealEventListFilterPanel();

   public MealEventListView() {
      setSizeFull();
      setSpacing(true);
      container.setBeanIdProperty("id");
      t.setSizeFull();
      t.setContainerDataSource(container);
      t.setColumnHeader("id", "ID");
      t.setColumnHeader("name", "Vardas");
      t.setColumnHeader("cardId", "Kortelės ID");
      t.setColumnHeader("date", "Data");
      t.setConverter("date", new StringToDateConverter() {
         @Override
         public DateFormat getFormat(Locale locale) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
         }
      });
      t.setVisibleColumns(new String[]{"id", "cardId", "name", "date"});
      t.setColumnWidth("cardId", 100);
      t.setColumnWidth("birthDate", 130);
      t.setColumnAlignment("cardId", Table.Align.CENTER);
      t.setColumnCollapsingAllowed(true);
      t.setColumnCollapsed("id", true);
      t.setSortContainerPropertyId("date");
      t.sort();
      t.setSelectable(true);
      t.setNullSelectionAllowed(false);
      t.setCacheRate(5);

      addComponents(filterPanel, t);
      setExpandRatio(t, 1f);
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
      t.addItemClickListener(listener);
   }

   public BeanContainer<Long, MealEvent> getContainer() {
      return container;
   }

   public MealEventListFilterPanel getFilterPanel() {
      return filterPanel;
   }


}
