package lt.pavilonis.cmm.common;

import com.vaadin.data.Binder;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;

public class FieldLayout<T> extends VerticalLayout {

   private final Label validationStatusLabel = new Label(null, ContentMode.HTML);

   public FieldLayout() {
      setMargin(false);
      addComponent(validationStatusLabel);
      validationStatusLabel.setVisible(false);
   }

   public void manualBinding(Binder<T> binder) {
   }

   public void initCustomFieldValues(T entity) {
   }

   protected String getFormCaption(Class<T> clazz) {
      return App.translate(clazz.getSimpleName());
   }

   public void displayErrorMessage(String errorMessage) {
      validationStatusLabel.setValue("<span style='color:red'>" + errorMessage + "</span>");
      validationStatusLabel.setVisible(true);
   }
}
