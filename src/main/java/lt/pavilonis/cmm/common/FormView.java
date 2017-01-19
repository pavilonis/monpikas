package lt.pavilonis.cmm.common;

import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class FormView<T> extends MVerticalLayout {

   public FormView() {
      setMargin(false);
   }

   public void manualBinding(MBeanFieldGroup<T> binding) {
   }

   public void initCustomFieldValues(T entity) {
   }
}
