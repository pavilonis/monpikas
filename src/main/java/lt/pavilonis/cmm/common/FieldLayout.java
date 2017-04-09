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

   public void manualBinding(Binder<T> binding) {
   }

   public void initCustomFieldValues(T entity) {
   }

   protected String getFormCaption() {
      return App.translate(this, "caption");
   }

   public void displayErrorMessage(String errorMessage) {
      validationStatusLabel.setValue("<h3>" + errorMessage + "</h3>");
      validationStatusLabel.setVisible(true);
   }
}
