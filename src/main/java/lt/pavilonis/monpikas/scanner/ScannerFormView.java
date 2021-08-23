package lt.pavilonis.monpikas.scanner;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.common.FieldLayout;

public class ScannerFormView extends FieldLayout<Scanner> {

   private final TextField name = new TextField();

   public ScannerFormView() {
      name.setWidth(268, Unit.PIXELS);
      addComponent(name);
      setMargin(new MarginInfo(false, false, true, false));
   }
}
