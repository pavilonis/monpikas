package lt.pavilonis.cmm.warehouse.receipt;

import com.google.common.collect.Lists;
import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.util.List;

public class ReceiptFilterPanel extends PeriodFilterPanel<ReceiptFilter> {

   private ComboBox<Supplier> supplierComboBox;

   public ReceiptFilterPanel(List<Supplier> suppliers) {
      this.supplierComboBox.setItems(suppliers);
   }

   @Override
   public ReceiptFilter getFilter() {
      return new ReceiptFilter(
            null,
            getPeriodStart().getValue(),
            getPeriodEnd().getValue(),
            supplierComboBox.getValue()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> fields = Lists.newArrayList(super.getFields());
      fields.add(supplierComboBox = new ComboBox<>(App.translate(getClass(), "supplier")));
      return fields;
   }
}
