package lt.pavilonis.cmm.common;

import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.fields.MTable;

import java.util.Collections;
import java.util.List;

public abstract class ListTable<T> extends MTable<T> {

   private MessageSourceAdapter messageSource = App.context.getBean(MessageSourceAdapter.class);

   public ListTable(Class<T> type) {
      super(type);

      List<String> properties = getProperties();
      withProperties(properties);

      String[] translatedPropertyArray = properties.stream()
            .map(property -> messageSource.get(type, property))
            .toArray(String[]::new);

      withColumnHeaders(translatedPropertyArray);

      defaultConfiguration();
      customize(messageSource);
   }

   protected abstract List<String> getProperties();

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
}
