package lt.pavilonis.cmm.warehouse.techcard.field;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.component.GridControlPanel;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class TechCardProductGroupOutputPopup extends Window {

   private final TextField outputWeight = new TextField(App.translate(TechCard.class, "outputWeight"));
   private final ComboBox<ProductGroup> productGroupComboBox;
   private final Consumer<Set<TechCardProductGroupOutput>> selectionConsumer;

   public TechCardProductGroupOutputPopup(List<ProductGroup> productGroups,
                                          Consumer<Set<TechCardProductGroupOutput>> selectionConsumer) {

      this.productGroupComboBox = new ComboBox<>(App.translate(TechCard.class, "productGroup"), productGroups);
      this.productGroupComboBox.setItemCaptionGenerator(ProductGroup::getName);
      this.selectionConsumer = selectionConsumer;

      GridControlPanel controls = new GridControlPanel(
            "addSelected", "close",
            click -> selectAction(),
            click -> close()
      );

      FormLayout layout = new FormLayout(productGroupComboBox, outputWeight, controls);
      layout.setMargin(true);

      setContent(layout);
      setModal(true);
      setWidth(512, Unit.PIXELS);
      UI.getCurrent().addWindow(this);
   }

   protected void selectAction() {
      String outputWeightText = outputWeight.getValue();
      ProductGroup productGroup = productGroupComboBox.getValue();

      if (productGroup == null || !NumberUtils.isParsable(outputWeightText)) {
         Notification.show("Incorrect values entered", Notification.Type.WARNING_MESSAGE);
         return;
      }

      int outputWeight = Integer.valueOf(outputWeightText);
      TechCardProductGroupOutput value = new TechCardProductGroupOutput(productGroup, outputWeight);
      selectionConsumer.accept(Collections.singleton(value));
      close();
   }
}
