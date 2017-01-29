package lt.pavilonis.cmm.common;

import lt.pavilonis.cmm.App;
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

   protected String getFormCaption() {
      return App.translate(this, "caption");
   }
}
