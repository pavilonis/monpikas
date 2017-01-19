package lt.pavilonis.cmm.common;

import org.vaadin.viritin.fields.MTable;

import java.util.Collections;
import java.util.List;

public class ListTable<T> extends MTable<T> {

   public ListTable(Class<T> type) {
      super(type);
      customize();
   }

   //TODO try to pass class to super constructor
   public ListTable() {
      customize();
   }

   protected void customize() {
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
