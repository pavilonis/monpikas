package lt.pavilonis.cmm.warehouse.product;

import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.util.Arrays;
import java.util.List;

public class ProductFilterPanel extends FilterPanel<ProductFilter> {

   private TextField textField;
   private ComboBox<ProductGroup> productGroupComboBox;

   ProductFilterPanel(List<ProductGroup> productGroups) {
      productGroupComboBox.setItems(productGroups);
      productGroupComboBox.setItemCaptionGenerator(ProductGroup::getName);
      productGroupComboBox.setCaption(App.translate(getClass(), "productGroup"));
   }

   @Override
   protected List<HasValue<?>> getFields() {
      return Arrays.asList(
            productGroupComboBox = new ComboBox<>(),
            textField = new ATextField(getClass(), "name")
      );
   }

   @Override
   public ProductFilter getFilter() {
      return new ProductFilter(textField.getValue(), productGroupComboBox.getValue());
   }
}
