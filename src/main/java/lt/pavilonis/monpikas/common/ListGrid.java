package lt.pavilonis.monpikas.common;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import lombok.extern.slf4j.Slf4j;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.service.MessageSourceAdapter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
public class ListGrid<T extends Identified<?>> extends Grid<T> {

   private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
   private static final String PROPERTY_ID = "ID";
   protected final MessageSourceAdapter messages = App.context.getBean(MessageSourceAdapter.class);
   private final List<T> items = new ArrayList<>();

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
      List<String> properties = getColumns().stream()
            .map(Column::getId)
            .collect(toUnmodifiableList());

      for (String property : properties) {

         Field field = getClassField(property);
         if (field == null) {
            // field can be null when column is hidden
            continue;
         }

         Class<?> classFieldType = field.getType();

         if (isBoolean(classFieldType)) {
            Method booleanGetter = createPropertyGetter(property, "is");

            replaceColumn(property, entity -> {
               Object booleanValue = extractPropertyValue(entity, booleanGetter);
               return Boolean.TRUE.equals(booleanValue) ? "✔" : "";
            });

            continue;
         }

         Method propertyGetter = createPropertyGetter(property, "get");

         if (Named.class.isAssignableFrom(classFieldType)) {
            replaceColumn(property, namedPrinter(propertyGetter));

         } else if (Collection.class.isAssignableFrom(classFieldType)) {
            replaceColumn(property, collectionPrinter(propertyGetter));

         } else if (LocalDateTime.class.isAssignableFrom(classFieldType)) {
            replaceColumn(property, dateTimePrinter(propertyGetter));
         }
      }
   }

   private Method createPropertyGetter(String property, String prefix) {
      String methodName = prefix + StringUtils.capitalize(property);
      return BeanUtils.findMethod(getBeanType(), methodName);
   }

   private boolean isBoolean(Class<?> classFieldType) {
      return Boolean.class.isAssignableFrom(classFieldType)
            || boolean.class.isAssignableFrom(classFieldType);
   }

   private void replaceColumn(String property, ValueProvider<T, Object> valueProvider) {
      removeColumn(property);
      addColumn(property, valueProvider);
   }

   private ValueProvider<T, Object> collectionPrinter(Method propertyGetter) {
      return entity -> {

         Collection<?> collection = (Collection) extractPropertyValue(entity, propertyGetter);
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

         Object propertyValue = extractPropertyValue(entity, propertyGetter);

         Method nameGetter = BeanUtils.findMethod(Named.class, "getName");

         return extractPropertyValue(propertyValue, nameGetter);
      };
   }

   private ValueProvider<T, Object> dateTimePrinter(Method propertyGetter) {
      return entity -> {
         LocalDateTime value = (LocalDateTime) extractPropertyValue(entity, propertyGetter);
         return value == null ? null : DATE_TIME_FORMATTER.format(value);
      };
   }

   private Object extractPropertyValue(Object object, Method propertyGetter) {
      try {
         return propertyGetter.invoke(object);
      } catch (IllegalAccessException | InvocationTargetException e) {
         log.error("Could not extract property value", e);
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
      return List.of();
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
      return List.of();
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
      return Map.of();
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

   public List<T> getItems() {
      return items;
   }
}
