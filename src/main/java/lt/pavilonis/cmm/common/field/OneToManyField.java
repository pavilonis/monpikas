package lt.pavilonis.cmm.common.field;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.Identifiable;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.component.GridControlPanel;
import lt.pavilonis.cmm.common.service.RepositoryFinder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class OneToManyField<T extends Identifiable<?>> extends CustomField<Collection<T>> {

   private final ListGrid<T> grid;
   private final Class<T> type;

   public OneToManyField(Class<T> type) {
      this(type, Collections.emptyMap());
   }

   public OneToManyField(Class<T> type, Map<String, ValueProvider<T, ?>> customColumns) {
      this.grid = createGrid(type, customColumns);
      this.type = type;
   }

   @Override
   protected void doSetValue(Collection<T> value) {
      this.grid.setItems(value);
   }

   protected ListGrid<T> createGrid(Class<T> type, Map<String, ValueProvider<T, ?>> customColumns) {
      ListGrid<T> grid = new ListGrid<T>(type) {
         @Override
         protected Map<String, ValueProvider<T, ?>> getCustomColumns() {
            return customColumns;
         }
      };
      grid.setWidth(512, Unit.PIXELS);
      grid.setHeight(330, Unit.PIXELS);
      return grid;
   }

   @Override
   protected Component initContent() {
      GridControlPanel controls = new GridControlPanel(
            eventAdd -> actionAdd(),
            eventRemove -> actionRemove()
      );
      VerticalLayout layout = new VerticalLayout(grid, controls);
      layout.setMargin(false);
      return layout;
   }

   private void actionAdd() {
      Consumer<Set<T>> selectionConsumer = items -> {
         boolean duplicatesFound = false;
         for (T item : items) {
            if (grid.hasItem(item)) {
               duplicatesFound = true;
            } else {
               grid.addItem(item);
            }
         }
         if (duplicatesFound) {
            Notification.show("Some values not added (already in the list)", Type.WARNING_MESSAGE);
         }
      };

      new SelectionPopup(selectionConsumer);
   }

   private void actionRemove() {
      Set<T> selectedItems = grid.getSelectedItems();
      if (CollectionUtils.isEmpty(selectedItems)) {
         Notification.show("Nothing selected!", Type.WARNING_MESSAGE);
      } else {
         selectedItems.forEach(grid::removeItem);
      }
   }

   protected List<T> getSelectionElements() {
      EntityRepository<T, ?, ?> repository = RepositoryFinder.find(type);
      return repository.load(null); //TODO null?
   }

   @Override
   public Collection<T> getValue() {
      return grid.getItems();
   }

   private class SelectionPopup extends Window {

      private SelectionPopup(Consumer<Set<T>> selectionConsumer) {

         setCaption(App.translate(SelectionPopup.class, "caption"));
         setWidth(700, Unit.PIXELS);
         setHeight(490, Unit.PIXELS);

         ListGrid<T> selectionTable = new ListGrid<>(type);
         selectionTable.setItems(getSelectionElements());
         selectionTable.addItemClickListener(click -> {
            if (click.getMouseEventDetails().isDoubleClick()) {
               selectAction(selectionConsumer, Collections.singleton(click.getItem()));
            }
         });

         GridControlPanel controls = new GridControlPanel(
               "addSelected", "close",
               click -> selectAction(selectionConsumer, selectionTable.getSelectedItems()),
               click -> close()
         );
         VerticalLayout layout = new VerticalLayout(selectionTable, controls);
         layout.setSizeFull();
         layout.setExpandRatio(selectionTable, 1f);
         setContent(layout);
         setModal(true);

         UI.getCurrent().addWindow(this);
      }

      protected void selectAction(Consumer<Set<T>> selectionConsumer, Set<T> selected) {
         if (CollectionUtils.isNotEmpty(selected)) {
            selectionConsumer.accept(selected);
         }
         close();
      }
   }

   public void setTableHeight(float value, Unit unit) {
      grid.setHeight(value, unit);
   }

   public void setTableWidth(int size, Unit units) {
      grid.setWidth(size, units);
   }
}
