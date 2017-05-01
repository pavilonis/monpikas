package lt.pavilonis.cmm.warehouse.product;

import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.warehouse.MeasureUnit;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;

import java.util.List;

public class ProductForm extends FieldLayout<Product> {
   private final TextField name = new ATextField(Product.class, "name");
   private final TextField unitWeight = new ATextField(Product.class, "unitWeight");
   private final EnumComboBox<MeasureUnit> measureUnit = new EnumComboBox<>(MeasureUnit.class);
   @PropertyId("productGroup")
   private final ComboBox<ProductGroup> productGroup;

   public ProductForm(List<ProductGroup> productGroups) {
      productGroup = new ComboBox<>(App.translate(Product.class, "productGroup"), productGroups);
      productGroup.setItemCaptionGenerator(Named::getName);
      addComponents(name, unitWeight, measureUnit, productGroup);
   }

   @Override
   public void manualBinding(Binder<Product> binding) {
      binding.bind(unitWeight,
            product -> String.valueOf(product.getUnitWeight()),
            (product, string) -> product.setUnitWeight(Integer.valueOf(string))
      );
      binding.bind(measureUnit, Product::getMeasureUnit, Product::setMeasureUnit);
      binding.bind(productGroup, Product::getProductGroup, Product::setProductGroup);
   }
}
