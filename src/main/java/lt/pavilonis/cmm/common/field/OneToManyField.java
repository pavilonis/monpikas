package lt.pavilonis.cmm.common.field;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.component.GridControlPanel;
import lt.pavilonis.cmm.common.service.RepositoryFinder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class OneToManyField<T extends Identified<?>> extends CustomField<Collection<T>> {

   private final ListGrid<T> grid;
   private final Class<T> type;
   private final Map<String, ValueProvider<T, ?>> customColumns;
   private final List<String> columnOrder;

   public OneToManyField(Class<T> type) {
      this(type, Collections.emptyMap());
   }

   public OneToManyField(Class<T> type, String... columnOrder) {
      this(type, Collections.emptyMap(), columnOrder);
   }

   public OneToManyField(Class<T> type,
                         Map<String, ValueProvider<T, ?>> customColumns) {
      this(type, customColumns, new String[0]);
   }

   public OneToManyField(Class<T> type,
                         Map<String, ValueProvider<T, ?>> customColumns,
                         String... columnOrder) {

      this.customColumns = customColumns;
      this.columnOrder = List.of(columnOrder);
      this.grid = createGrid(type);
      this.type = type;
   }

   @Override
   protected void doSetValue(Collection<T> value) {
      this.grid.setItems(value);
   }

   protected ListGrid<T> createGrid(Class<T> type) {
      ListGrid<T> grid = new ListGrid<T>(type) {
         @Override
         protected Map<String, ValueProvider<T, ?>> getCustomColumns() {
            return customColumns;
         }

         @Override
         protected List<String> columnOrder() {
            return columnOrder;
         }
      };
      grid.setSizeFull();
      return grid;
   }

   @Override
   protected Component initContent() {
      VerticalLayout layout = new VerticalLayout(grid, createControls());
      layout.setMargin(false);
      return layout;
   }

   protected Component createControls() {
      return new GridControlPanel(
            eventAdd -> actionAdd(),
            eventRemove -> actionRemove()
      );
   }

   protected void actionAdd() {
      Consumer<Set<T>> selectionConsumer = createSelectionConsumer();
      new SelectionPopup(selectionConsumer);
   }

   protected Consumer<Set<T>> createSelectionConsumer() {
      return items -> {

         ArrayList<T> oldValue = new ArrayList<>(grid.getItems());

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

         fireEvent(createValueChange(oldValue, true));
      };
   }

   private void actionRemove() {

      ArrayList<T> oldValue = new ArrayList<>(grid.getItems());

      Set<T> selectedItems = grid.getSelectedItems();
      if (CollectionUtils.isEmpty(selectedItems)) {
         Notification.show("Nothing selected!", Type.WARNING_MESSAGE);
      } else {
         selectedItems.forEach(grid::removeItem);
      }

      fireEvent(createValueChange(oldValue, true));
   }

   protected List<T> getSelectionElements() {
      EntityRepository<T, ?, ?> repository = RepositoryFinder.find(type);
      return repository.load();
   }

   @Override
   public Collection<T> getValue() {
      return grid.getItems();
   }

   private class SelectionPopup extends Window {

      private SelectionPopup(Consumer<Set<T>> selectionConsumer) {

         setCaption(App.translate(SelectionPopup.class, "caption"));
         setWidth(900, Unit.PIXELS);
         setHeight(490, Unit.PIXELS);

         ListGrid<T> selectionTable = new ListGrid<T>(type) {
            @Override
            protected Map<String, ValueProvider<T, ?>> getCustomColumns() {
               return customColumns;
            }

            @Override
            protected List<String> columnOrder() {
               return getSelectionPopupColumnOrder();
            }
         };
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
         if (!CollectionUtils.isEmpty(selected)) {
            selectionConsumer.accept(selected);
         }
         close();
      }
   }

   protected List<String> getSelectionPopupColumnOrder() {
      return columnOrder;
   }

   public void setTableHeight(float value, Unit unit) {
      grid.setHeight(value, unit);
   }

   public void setTableWidth(int size, Unit units) {
      grid.setWidth(size, units);
   }

   public void addStyleGenerator(StyleGenerator<T> styleGenerator) {
      this.grid.setStyleGenerator(styleGenerator);
   }

   public Grid.Column<T, ?> getColumn(String columnName) {
      return grid.getColumn(columnName);
   }
}
