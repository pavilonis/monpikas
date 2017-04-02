package lt.pavilonis.cmm.common;

import com.vaadin.data.Binder;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;

public class FormView<T> extends VerticalLayout {

   public FormView() {
      setMargin(false);
   }

   public void manualBinding(Binder<T> binding) {
   }

   public void initCustomFieldValues(T entity) {
   }

   protected String getFormCaption() {
      return App.translate(this, "caption");
   }
}
