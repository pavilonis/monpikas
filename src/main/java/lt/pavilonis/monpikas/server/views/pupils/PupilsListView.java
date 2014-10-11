package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;

public class PupilsListView extends VerticalLayout {

   private Table t = new Table();
   BeanContainer<Long, AdbPupilDto> container = new BeanContainer<>(AdbPupilDto.class);
   PupilListFilterPanel pupilListFilterPanel = new PupilListFilterPanel();

   public PupilsListView() {
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
      t.setColumnHeader("breakfastPermitted", "Pusryčiai");
      t.setColumnHeader("comment", "Komentaras");
      t.setVisibleColumns(
            new String[]{"cardId", "firstName", "lastName", "birthDate", "breakfastPermitted", "dinnerPermitted", "comment"}
      );
      t.setColumnWidth("dinnerPermitted", 85);
      t.setColumnWidth("breakfastPermitted", 85);
      t.setConverter("dinnerPermitted", newStringToBoolConverter());
      t.setConverter("breakfastPermitted", newStringToBoolConverter());
      t.setColumnWidth("birthDate", 130);
      t.setColumnWidth("cardId", 90);
      t.setColumnAlignment("dinnerPermitted", Table.Align.CENTER);
      t.setColumnAlignment("breakfastPermitted", Table.Align.CENTER);
      t.setColumnAlignment("birthDate", Table.Align.CENTER);
      t.setColumnCollapsingAllowed(true);
      t.setColumnCollapsed("cardId", true);
      t.setSelectable(true);
      t.setNullSelectionAllowed(false);
      t.setCacheRate(5);
      addComponents(pupilListFilterPanel, t);
      setExpandRatio(t, 1f);
      pupilListFilterPanel.addFilterButtonListener(filterButtonClicked -> {
               container.removeAllContainerFilters();
               container.addContainerFilter(pupilListFilterPanel.getFilter());
            }
      );
      pupilListFilterPanel.addCancelFilterButtonListener(cancelFilterButtonClicked -> {
         pupilListFilterPanel.cleanFields();
         container.removeAllContainerFilters();
      });
   }

   public void setTableClickListener(ItemClickListener listener) {
      t.addItemClickListener(listener);
   }

   public BeanContainer<Long, AdbPupilDto> getContainer() {
      return container;
   }

   public PupilListFilterPanel getPupilListFilterPanel() {
      return pupilListFilterPanel;
   }

   private StringToBooleanConverter newStringToBoolConverter() {
      return new StringToBooleanConverter() {
         @Override
         protected String getTrueString() {
            return "✔";
         }

         @Override
         protected String getFalseString() {
            return "";
         }
      };
   }
}
