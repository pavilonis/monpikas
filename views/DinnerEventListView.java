package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.DinnerEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DinnerEventListView extends VerticalLayout {

   private Table t = new Table();
   BeanContainer<Long, DinnerEvent> container = new BeanContainer<>(DinnerEvent.class);
   DinnerListFilterPanel filterPanel = new DinnerListFilterPanel();

   public DinnerEventListView() {
      setSizeFull();
      setSpacing(true);
      container.setBeanIdProperty("id");
      t.setSizeFull();
      t.setContainerDataSource(container);
      t.setColumnHeader("id", "ID");
      t.setColumnHeader("name", "Vardas");
      t.setColumnHeader("cardId", "Kortelės #");
      t.setColumnHeader("date", "Data");
      t.setConverter("date", new StringToDateConverter(){
         @Override
         public DateFormat getFormat(Locale locale){
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
         }
      });
      t.setVisibleColumns(new String[]{"id", "cardId", "name", "date"});
      t.setColumnWidth("cardId", 90);
      t.setColumnWidth("birthDate", 130);
      t.setColumnAlignment("cardId", Table.Align.CENTER);
      t.setColumnCollapsingAllowed(true);
      t.setColumnCollapsed("id", true);
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
   }

   public void setTableClickListener(ItemClickListener listener) {
      t.addItemClickListener(listener);
   }

   public BeanContainer<Long, DinnerEvent> getContainer() {
      return container;
   }

   public DinnerListFilterPanel getFilterPanel() {
      return filterPanel;
   }


}
