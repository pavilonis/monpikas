package lt.pavilonis.cmm.common;

import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListGrid<T> extends Grid<T> {

   protected final MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);
   private Collection<T> items;

   public ListGrid(Class<T> type) {
      super(type);

      List<String> properties = getProperties(type);
      if (CollectionUtils.isNotEmpty(properties)) {
         getColumns().stream()
               .map(Column::getId)
               .filter(columnId -> !properties.contains(columnId))
               .forEach(this::removeColumn);
      }

      addCustomColumns();
      addColumns(properties);

      if (!properties.isEmpty()) {
         setColumnOrder(properties.toArray(new String[properties.size()]));
      }


      String[] headers = getColumns().stream()
            .map(Column::getId)
            .map(property -> App.translate(type, property))
            .toArray(String[]::new);

      setHeaders(headers);

      defaultConfiguration();
      customize();
   }

   protected List<String> getProperties(Class<T> type) {
      return Collections.emptyList();
//      return PropertyCollector.collect(type);
   }

   protected void customize() {/*hook*/}

   private void defaultConfiguration() {
//      collap(true);
      setColumnReorderingAllowed(true);
//      setSelectable(true);
//      setCacheRate(5);

      setSelectionMode(SelectionMode.SINGLE);
      //TODO any other way?
      ((SingleSelectionModel) getSelectionModel()).setDeselectAllowed(false);
      setSizeFull();
   }

   protected List<String> columnsToCollapse() {
      return Collections.emptyList();
   }

   public void collapseColumns() {
//      columnsToCollapse().forEach(
//            columnId -> setColumnCollapsed(columnId, true)
//      );
   }

   @Override
   public void setItems(Collection<T> items) {
      this.items = items;
      super.setItems(items);
   }

   public void removeItem(T item) {
      this.items.remove(item);
//      setItems(this.items);
   }

   public void addItem(T item) {
      this.items.add(item);
//      setItems(this.items);
      getDataProvider().refreshAll();
   }

   //TODO return type as map, to force setting IDs ?
   protected void addCustomColumns() {
   }

   public void addColumns(List<String> properties) {

      List<String> existingColumns = getColumns().stream()
            .map(Column::getId)
            .collect(Collectors.toList());

      properties.stream()
            .filter(property -> !existingColumns.contains(property))
            .forEach(this::addColumn);
   }

//   public void addColumns(String... properties) {
//      addColumns(Arrays.asList(properties));
//   }

   public void setHeaders(String... headers) {
      List<Column<T, ?>> columns = getColumns();
      if (headers.length != columns.size()) {
         throw new IllegalArgumentException("Number of headers must match number or columns");
      }

      for (int i = 0; i < columns.size(); i++) {
         columns.get(i).setCaption(headers[i]);
      }
   }

   public boolean hasItem(T item) {
      return this.items.contains(item);
   }
}
