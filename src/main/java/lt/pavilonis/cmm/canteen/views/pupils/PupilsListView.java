package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.views.converters.CollectionCellConverter;

import static com.vaadin.ui.Table.Align.CENTER;

public class PupilsListView extends VerticalLayout {

   private final BeanContainer<String, Pupil> container = new BeanContainer<>(Pupil.class);
   private final Table table = new PupilsTable(container);
   private final PupilListFilterPanel pupilListFilterPanel = new PupilListFilterPanel();

   public PupilsListView() {
      setSizeFull();
      addComponents(pupilListFilterPanel, table);
      setExpandRatio(table, 1f);

      pupilListFilterPanel.addFilterButtonListener(click -> {
         container.removeAllContainerFilters();
         container.addContainerFilter(pupilListFilterPanel.getFilter());
      });

      pupilListFilterPanel.addCancelFilterButtonListener(click -> {
         pupilListFilterPanel.cleanFields();
         container.removeAllContainerFilters();
      });
   }

   public void setTableClickListener(ItemClickListener listener) {
      table.addItemClickListener(listener);
   }

   public BeanContainer<String, Pupil> getContainer() {
      return container;
   }

   private class PupilsTable extends Table {
      public PupilsTable(BeanContainer<String, Pupil> container) {
         setSizeFull();
         container.setBeanIdProperty("cardCode");
         setContainerDataSource(container);

         setVisibleColumns("cardCode", "firstName", "lastName", "birthDate", "grade", "comment", "meals");
         setColumnHeaders("Kodas", "Vardas", "Pavardė", "Gimimo data", "Klasė", "Komentaras", "Porcijos");

         setColumnWidth("grade", 85);
         setColumnWidth("birthDate", 130);
         setColumnWidth("cardCode", 90);

         setConverter("meals", new CollectionCellConverter());

         setColumnAlignment("birthDate", CENTER);

         setColumnCollapsingAllowed(true);
         setColumnCollapsed("cardCode", true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
//         setSortContainerPropertyId("firstName");
      }
   }
}
