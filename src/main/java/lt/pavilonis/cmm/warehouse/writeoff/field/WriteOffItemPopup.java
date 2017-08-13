package lt.pavilonis.cmm.warehouse.writeoff.field;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.component.GridControlPanel;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.writeoff.WriteOffItem;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WriteOffItemPopup extends Window {

   private final TextField unitWeight = new ATextField(getClass(), "unitWeight");
   private final TextField measureUnit = new ATextField(getClass(), "measureUnit");

   private final TextField quantity = new ATextField(getClass(), "quantity");
   private final TextField unitPrice = new ATextField(getClass(), "unitPrice");

   private final ComboBox<Product> productComboBox;
   private final ComboBox<ProductGroup> productGroupComboBox;
   private final Consumer<Set<WriteOffItem>> selectionConsumer;

   public WriteOffItemPopup(Collection<Product> products, Collection<ProductGroup> productGroups,
                            Consumer<Set<WriteOffItem>> selectionConsumer) {

      this.productGroupComboBox = new ComboBox<>(App.translate(WriteOffItemPopup.class, "productGroupFilter"), productGroups);
      this.productComboBox = new ComboBox<>(App.translate(WriteOffItemPopup.class, "product"), products);
      this.selectionConsumer = selectionConsumer;

      productGroupComboBox.setItemCaptionGenerator(ProductGroup::getName);
      productGroupComboBox.setIcon(VaadinIcons.FILTER);
      productGroupComboBox.addValueChangeListener(event -> {
         ProductGroup groupFilter = event.getValue();

         productComboBox.clear();

         if (groupFilter == null) {
            productComboBox.setItems(products);

         } else {
            List<Product> filteredProducts = products.stream()
                  .filter(product -> groupFilter.getId().equals(product.getProductGroup().getId()))
                  .sorted(Comparator.comparing(Product::getName))
                  .collect(Collectors.toList());
            productComboBox.setItems(filteredProducts);
         }
      });

      productComboBox.addValueChangeListener(event -> {
         Product product = event.getValue();
         if (event.getValue() == null) {
            unitWeight.clear();
            measureUnit.clear();
         } else {
            unitWeight.setValue(String.valueOf(product.getUnitWeight()));
            measureUnit.setValue(App.translate(MeasureUnit.class, product.getMeasureUnit().name()));
         }
      });

      productComboBox.setItemCaptionGenerator(Product::getName);
      unitWeight.setReadOnly(true);
      measureUnit.setReadOnly(true);

      FormLayout layout = new FormLayout(
            productGroupComboBox,
            productComboBox,
            measureUnit,
            unitWeight,
            unitPrice,
            quantity,
            new GridControlPanel("add", "close", click -> {/*TODO*/}, click -> close())
      );
      layout.setMargin(true);

      setContent(layout);
      setModal(true);
      setCaption(App.translate(getClass(), "caption"));
      setWidth(540, Unit.PIXELS);
      UI.getCurrent().addWindow(this);
   }
}
