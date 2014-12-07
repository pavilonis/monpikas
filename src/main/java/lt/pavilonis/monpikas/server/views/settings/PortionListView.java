package lt.pavilonis.monpikas.server.views.settings;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.views.converters.PortionTypeCellConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PortionListView extends VerticalLayout {

   private Table table = new Table();
   BeanContainer<Long, Portion> container = new BeanContainer<>(Portion.class);
   PortionListControlPanel controlPanel = new PortionListControlPanel();

   public PortionListView() {
      setMargin(true);
      setSizeFull();
      container.setBeanIdProperty("id");
      table.setSizeFull();
      table.setContainerDataSource(container);
      container.sort(new Object[]{"id"}, new boolean[]{true});

      table.setVisibleColumns("id", "name", "type", "price");
      table.setColumnHeaders("Id", "Pavadinimas", "Tipas", "Kaina");

      table.setConverter("type", new PortionTypeCellConverter());
      table.setConverter("price", new StringToDoubleConverter() {
         @Override
         protected NumberFormat getFormat(Locale locale) {
            return new DecimalFormat("0.00");
         }
      });

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
