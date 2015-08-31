package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import lt.pavilonis.monpikas.server.views.converters.CollectionCellConverter;
import lt.pavilonis.monpikas.server.views.converters.OptionalCellConverter;
import lt.pavilonis.monpikas.server.views.converters.SimpleStringToLongConverter;

import static com.vaadin.ui.Table.Align.CENTER;

public class PupilsListView extends VerticalLayout {

   private final BeanContainer<Long, PupilDto> container = new BeanContainer<>(PupilDto.class);
   private final Table table = new PupilsTable(container);
   private final PupilListFilterPanel pupilListFilterPanel = new PupilListFilterPanel();

   public PupilsListView() {
      setSizeFull();
      addComponents(pupilListFilterPanel, table);
      setExpandRatio(table, 1f);

      pupilListFilterPanel.addFilterButtonListener(filterButtonClicked -> {
         container.removeAllContainerFilters();
         container.addContainerFilter(pupilListFilterPanel.getFilter());
      });

      pupilListFilterPanel.addCancelFilterButtonListener(cancelFilterButtonClicked -> {
         pupilListFilterPanel.cleanFields();
         container.removeAllContainerFilters();
      });
   }

   public void setTableClickListener(ItemClickListener listener) {
      table.addItemClickListener(listener);
   }

   public BeanContainer<Long, PupilDto> getContainer() {
      return container;
   }


   private class PupilsTable extends Table {
      public PupilsTable(BeanContainer<Long, PupilDto> container) {
         setSizeFull();
         container.setBeanIdProperty("cardId");
         setContainerDataSource(container);

         setVisibleColumns("cardId", "firstName", "lastName", "birthDate", "grade", "comment", "meals");
         setColumnHeaders("Kortelės nr.", "Vardas", "Pavardė", "Gimimo data", "Klasė", "Komentaras", "Porcijos");

         setColumnWidth("grade", 85);
         setColumnWidth("birthDate", 130);
         setColumnWidth("cardId", 90);

         setConverter("cardId", new SimpleStringToLongConverter());
         setConverter("birthDate", new OptionalCellConverter());
         setConverter("comment", new OptionalCellConverter());
         setConverter("meals", new CollectionCellConverter());

         setColumnAlignment("birthDate", CENTER);

         setColumnCollapsingAllowed(true);
         setColumnCollapsed("cardId", true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
      }
   }
}
