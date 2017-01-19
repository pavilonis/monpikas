package lt.pavilonis.cmm.common;

import org.vaadin.viritin.fields.MTable;

import java.util.Collections;
import java.util.List;

public class ListTable<T> extends MTable<T> {

   //TODO try to pass class to super constructor
   public ListTable() {
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

}
