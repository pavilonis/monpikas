package lt.pavilonis.monpikas.server.views;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.dao.PupilDto;

public class TablePanel extends Panel {

   private Table t = new Table();
   BeanContainer<Long, PupilDto> container = new BeanContainer<>(PupilDto.class);
   FilterPanel filterPanel = new FilterPanel();

   public TablePanel() {

      container.setBeanIdProperty("cardId");
      t.setContainerDataSource(container);
      t.setColumnHeader("cardId", "Kortelkos ID");
      t.setColumnHeader("firstName", "Vardas");
      t.setColumnHeader("lastName", "Pavarde");
      t.setColumnHeader("birthDate", "Gimimo data");
      t.setColumnHeader("dinner", "Pietus");
      t.setColumnHeader("comment", "Komentaras");
      t.setVisibleColumns(new String[]{"cardId", "firstName", "lastName", "birthDate", "dinner", "comment"});
      t.setColumnCollapsingAllowed(true);
      t.setColumnCollapsed("cardId", true);
      t.setSizeFull();
      t.setPageLength(22);
      t.setSelectable(true);
      t.setNullSelectionAllowed(false);
      t.setCacheRate(5);
      t.setConverter("dinner", new StringToBooleanConverter() {
         @Override
         protected String getTrueString() {
            return "✔";
         }

         @Override
         protected String getFalseString() {
            return "";
         }
      });
      VerticalLayout vl = new VerticalLayout(filterPanel, t);
      setContent(vl);
      setSizeFull();
   }

   public void setTableClickListener(ItemClickListener listener) {
      t.addItemClickListener(listener);
   }

   public BeanContainer<Long, PupilDto> getContainer() {
      return container;
   }

   public FilterPanel getFilterPanel() {
      return filterPanel;
   }
}
