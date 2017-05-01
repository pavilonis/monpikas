package lt.pavilonis.cmm.warehouse.productgroup;

import com.vaadin.data.Binder;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;

public class ProductGroupForm extends FieldLayout<ProductGroup> {

   private final TextField name = new ATextField(ProductGroup.class, "name");
   private final TextField kcal100 = new ATextField(ProductGroup.class, "kcal100");

   public ProductGroupForm() {
      addComponents(name, kcal100);
   }

   @Override
   public void manualBinding(Binder<ProductGroup> binding) {
      binding.bind(kcal100,
            group -> String.valueOf(group.getKcal100()),
            (group, string) -> group.setKcal100(Integer.valueOf(string))
      );
   }
}
