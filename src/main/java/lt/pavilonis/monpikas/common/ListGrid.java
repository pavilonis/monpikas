package lt.pavilonis.monpikas.common;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.service.MessageSourceAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListGrid<T extends Identified<?>> extends Grid<T> {

   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
   private static final String PROPERTY_ID = "ID";
   protected final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);
   private List<T> items = new ArrayList<>();

   public ListGrid(Class<T> type) {
      super(type);
      super.setItems(items);

      List<String> properties = columnOrder();
      Map<String, ValueProvider<T, ?>> customColumns = getCustomColumns();

      getColumns().stream()
            .map(Column::getId)
            .filter(column ->
                  (!properties.isEmpty() && !properties.contains(column)) || customColumns.containsKey(column))
            .forEach(this::removeColumn);

      configureNamedEntityColumns();

      addColumns(properties, customColumns);

      if (!properties.isEmpty()) {
         setColumnOrder(properties.toArray(new String[properties.size()]));
      }

      String[] headers = translateHeaders(type);
      setHeaders(headers);

      defaultConfiguration();
      customize();
      collapseColumns();
   }

   private void configureNamedEntityColumns() {
      getColumns().stream()
            .map(Column::getId)
            .forEach(property -> {
               Field field = getClassField(property);

               if (field != null) {
                  Class<?> classFieldType = field.getType();

                  Method propertyGetter = BeanUtils.findMethod(
                        getBeanType(),
                        "get" + StringUtils.capitalize(property)
                  );

                  if (Named.class.isAssignableFrom(classFieldType)) {

                     replaceColumn(property, namedPrinter(propertyGetter));

                  } else if (Collection.class.isAssignableFrom(classFieldType)) {

                     replaceColumn(property, collectionPrinter(propertyGetter));

                  } else if (Boolean.class.isAssignableFrom(classFieldType)
                        || boolean.class.isAssignableFrom(classFieldType)) {

                     replaceColumn(property, entity ->
                           Boolean.TRUE.equals(extractProperty(entity, propertyGetter)) ? "✔" : "");

                  } else if (LocalDateTime.class.isAssignableFrom(classFieldType)) {

                     replaceColumn(property, dateTimePrinter(propertyGetter));
                  }
               }
            });
   }

   private void replaceColumn(String property, ValueProvider<T, Object> valueProvider) {
      removeColumn(property);
      addColumn(property, valueProvider);
   }

   private ValueProvider<T, Object> collectionPrinter(Method propertyGetter) {
      return entity -> {

         Collection<?> collection = (Collection) extractProperty(entity, propertyGetter);
         if (CollectionUtils.isEmpty(collection)) {
            return "";
         }

         if (collection.iterator().next() instanceof Named) {
            String result = collection.stream()
                  .map(value -> (Named) value)
                  .map(Named::getName)
                  .collect(Collectors.joining(", "));

            return result.length() <= 50
                  ? result
                  : result.substring(0, 49) + "...";
         }

         return collection.toString();
      };
   }

   private ValueProvider<T, Object> namedPrinter(Method propertyGetter) {
      return entity -> {

         Object propertyValue = extractProperty(entity, propertyGetter);

         Method nameGetter = BeanUtils.findMethod(Named.class, "getName");

         return extractProperty(propertyValue, nameGetter);
      };
   }

   private ValueProvider<T, Object> dateTimePrinter(Method propertyGetter) {
      return entity -> {
         LocalDateTime value = (LocalDateTime) extractProperty(entity, propertyGetter);
         return value == null ? null : DATE_TIME_FORMATTER.format(value);
      };
   }

   private Object extractProperty(Object object, Method propertyGetter) {
      try {
         return propertyGetter.invoke(object);
      } catch (IllegalAccessException | InvocationTargetException e) {
         e.printStackTrace();
         return null;
      }
   }

   protected String[] translateHeaders(Class<T> type) {
      return getColumns().stream()
            .map(Column::getId)
            .map(property -> PROPERTY_ID.equals(property) ? PROPERTY_ID : App.translate(type, property))
            .toArray(String[]::new);
   }

   protected List<String> columnOrder() {
      return Collections.emptyList();
   }

   protected void customize() {/*hook*/}

   private void defaultConfiguration() {
      setColumnReorderingAllowed(true);
      setSelectionMode(SelectionMode.SINGLE);
      ((SingleSelectionModel) getSelectionModel()).setDeselectAllowed(false);
      setSizeFull();

      getColumns()
            .forEach(column -> column.setHidable(true));

      Grid.Column<T, ?> idColumn = getColumn("id");
      if (idColumn != null) {
         idColumn.setHidden(true);
      }
   }

   protected List<String> columnsToCollapse() {
      return Collections.emptyList();
   }

   public void collapseColumns() {
      columnsToCollapse().stream()
            .map(this::getColumn)
            .forEach(column -> column.setHidden(true));
   }

   @Override
   public void setItems(Collection<T> items) {
      this.items.clear();
      items.forEach(this.items::add);
      super.setItems(this.items);
   }

   protected Map<String, ValueProvider<T, ?>> getCustomColumns() {
      return Collections.emptyMap();
   }

   private void addColumns(List<String> properties, Map<String, ValueProvider<T, ?>> customColumns) {

      customColumns.forEach((id, valueProvider) -> {
         if (properties.isEmpty() || properties.contains(id)) {
            addColumn(id, valueProvider);
         }
      });

      List<String> existingColumns = getColumns().stream()
            .map(Column::getId)
            .collect(Collectors.toList());

      properties.stream()
            .filter(property -> !existingColumns.contains(property))
            .forEach(this::addColumn);
   }

   private void addColumn(String id, ValueProvider<T, ?> valueProvider) {
      Column<T, ?> column = addColumn(valueProvider);
      column.setId(id);
   }

   private Field getClassField(String property) {
      try {
         return getBeanType().getDeclaredField(property);
      } catch (NoSuchFieldException e) {
         return null;
      }
   }

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

   public void addOrUpdate(T itemOld, T itemNew) {
      if (getDataProvider() instanceof ListDataProvider) {
         if (itemOld.getId() != null) {
            int i = items.indexOf(itemOld);
            items.remove(i);
            items.add(i, itemNew);
         } else {
            items.add(itemNew);
         }
      }
      getDataProvider().refreshAll();
      select(itemNew);
   }

   public void removeItem(T item) {
      if (getDataProvider() instanceof ListDataProvider) {
         this.items.remove(item);
      }
      getDataProvider().refreshAll();
   }

   public void addItem(T item) {
      if (getDataProvider() instanceof ListDataProvider) {
         this.items.add(item);
      }
      getDataProvider().refreshAll();
   }

   public Collection<T> getItems() {
      return items;
   }
}
