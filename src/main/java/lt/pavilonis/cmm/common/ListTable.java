package lt.pavilonis.cmm.common;

import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.fields.MTable;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListTable<T> extends MTable<T> {

   private MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);


   public ListTable(Class<T> type) {
      super(type);

      List<String> properties = getProperties(type);
      withProperties(properties);

      String[] translatedPropertyArray = properties.stream()
            .map(property -> messageSource.get(type, property))
            .toArray(String[]::new);

      withColumnHeaders(translatedPropertyArray);

      defaultConfiguration();
      customize(messageSource);
   }

   protected List<String> getProperties(Class<T> type) {
      return collectProperties(type);
   }

   protected void customize(MessageSourceAdapter messageSource) {/*hook*/}

   private void defaultConfiguration() {
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setSelectable(true);
      setNullSelectionAllowed(false);
      setCacheRate(5);
      setSizeFull();
   }

   protected List<String> columnsToCollapse() {
      return Collections.emptyList();
   }

   public void collapseColumns() {
      columnsToCollapse().forEach(
            columnId -> setColumnCollapsed(columnId, true)
      );
   }

   protected List<String> collectProperties(Class<T> type) {
      Field[] fields = type.getFields();
      return Stream.of(fields)
            .map(Field::getName)
            .collect(Collectors.toList());
   }
}
