package lt.pavilonis.cmm.common;

import org.vaadin.viritin.fields.MTable;

import java.util.Collections;
import java.util.List;

public class ListTable<T> extends MTable<T> {

   protected List<String> columnsToCollapse() {
      return Collections.emptyList();
   }

}
