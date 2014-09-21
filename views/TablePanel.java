package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.AdbPupilDto;

public class TablePanel extends VerticalLayout {

   private Table t = new Table();
   BeanContainer<Long, AdbPupilDto> container = new BeanContainer<>(AdbPupilDto.class);
   FilterPanel filterPanel = new FilterPanel();

   public TablePanel() {
      setSizeFull();
      setSpacing(true);
      container.setBeanIdProperty("cardId");
      t.setSizeFull();
      t.setContainerDataSource(container);
      t.setColumnHeader("cardId", "Kortelės #");
      t.setColumnHeader("firstName", "Vardas");
      t.setColumnHeader("lastName", "Pavarde");
      t.setColumnHeader("birthDate", "Gimimo data");
      t.setColumnHeader("dinnerPermitted", "Pietus");
      t.setColumnHeader("comment", "Komentaras");
      t.setVisibleColumns(new String[]{"cardId", "firstName", "lastName", "birthDate", "dinnerPermitted", "comment"});
      t.setColumnWidth("dinnerPermitted", 70);
      t.setColumnWidth("birthDate", 130);
      t.setColumnAlignment("dinnerPermitted", Table.Align.CENTER);
      t.setColumnAlignment("birthDate", Table.Align.CENTER);
      t.setColumnCollapsingAllowed(true);
      t.setColumnCollapsed("cardId", true);
      t.setSelectable(true);
      t.setNullSelectionAllowed(false);
      t.setCacheRate(5);
      t.setConverter("dinnerPermitted", new StringToBooleanConverter() {
         @Override
         protected String getTrueString() {
            return "✔";
         }

         @Override
         protected String getFalseString() {
            return "";
         }
      });
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

   public BeanContainer<Long, AdbPupilDto> getContainer() {
      return container;
   }

   public FilterPanel getFilterPanel() {
      return filterPanel;
   }


}
