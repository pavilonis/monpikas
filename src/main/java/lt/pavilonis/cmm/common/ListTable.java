package lt.pavilonis.cmm.common;

import com.vaadin.ui.Grid;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListTable<T> extends Grid<T> {

   private MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);


   public ListTable(Class<T> type) {
      super(type);

      List<String> properties = getProperties(type);
      properties.forEach(
            this::addColumn
      );

      String[] translatedPropertyArray = properties.stream()
            .map(property -> messageSource.get(type, property))
            .toArray(String[]::new);

//      header
//      withColumnHeaders(translatedPropertyArray);

      defaultConfiguration();
      customize(messageSource);
   }

   protected List<String> getProperties(Class<T> type) {
      return collectProperties(type);
   }

   protected void customize(MessageSourceAdapter messageSource) {/*hook*/}

   private void defaultConfiguration() {
//      collap(true);
      setColumnReorderingAllowed(true);
//      setSelectable(true);
//      setNullSelectionAllowed(false);
//      setCacheRate(5);
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

   private List<String> collectProperties(Class<T> type) {
      return Stream.of(type.getMethods())
            .map(Method::getName)
            .filter(name -> name.startsWith("get") || name.startsWith("is"))
            .map(name -> name.startsWith("is") ? name.substring(2) : name.substring(3))
            .map(String::toLowerCase)
            .collect(Collectors.toList());
   }
}
