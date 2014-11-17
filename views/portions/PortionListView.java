package lt.pavilonis.monpikas.server.views.portions;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.views.converters.PortionTypeCellConverter;

public class PortionListView extends VerticalLayout {

   private Table table = new Table();
   BeanContainer<Long, Portion> container = new BeanContainer<>(Portion.class);
   PortionListControlPanel controlPanel = new PortionListControlPanel();

   public PortionListView() {
      setSizeFull();
      container.setBeanIdProperty("id");
      table.setSizeFull();
      table.setContainerDataSource(container);
      container.sort(new Object[]{"id"}, new boolean[]{true});
      table.setColumnHeader("id", "ID");
      table.setColumnHeader("name", "Pavadinimas");
      table.setColumnHeader("type", "Tipas");
      table.setColumnHeader("price", "Kaina (Lt.)");

      table.setVisibleColumns(new String[]{"id", "name", "type", "price"});
      table.setConverter("type", new PortionTypeCellConverter());
      table.setColumnCollapsingAllowed(true);
      table.setColumnCollapsed("id", true);
      table.setSelectable(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);

      addComponents(table, controlPanel);
      setExpandRatio(table, 1f);
   }

   public void setTableClickListener(ItemClickListener listener) {
      table.addItemClickListener(listener);
   }

   public BeanContainer<Long, Portion> getContainer() {
      return container;
   }

   public PortionListControlPanel getControlPanel() {
      return controlPanel;
   }

   public Table getTable() {
      return table;
   }
}
