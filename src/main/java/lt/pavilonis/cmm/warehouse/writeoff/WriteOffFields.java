package lt.pavilonis.cmm.warehouse.writeoff;

import com.vaadin.ui.DateField;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.warehouse.writeoff.field.WriteOffItemsField;

public class WriteOffFields extends FieldLayout<WriteOff> {

   private final DateField periodStart = new ADateField(getClass(), "periodStart");
   private final DateField periodEnd = new ADateField(getClass(), "periodEnd");

   private final OneToManyField<WriteOffItem> items;

   WriteOffFields() {
      this.items = new WriteOffItemsField();

      addComponents(periodStart, periodEnd, items);
   }
}
