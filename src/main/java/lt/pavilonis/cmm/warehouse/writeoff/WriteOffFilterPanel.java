package lt.pavilonis.cmm.warehouse.writeoff;

import com.google.common.collect.Lists;
import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.cmm.warehouse.supplier.Supplier;

import java.util.List;

public class WriteOffFilterPanel extends PeriodFilterPanel<WriteOffFilter> {

   @Override
   public WriteOffFilter getFilter() {
      return new WriteOffFilter(
            null,
            getPeriodStart().getValue(),
            getPeriodEnd().getValue()
      );
   }

   @Override
   // boolean "confirmed" field may be added later
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> fields = Lists.newArrayList(super.getFields());
      return fields;
   }
}
