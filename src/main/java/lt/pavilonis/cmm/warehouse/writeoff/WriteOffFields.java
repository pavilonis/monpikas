package lt.pavilonis.cmm.warehouse.writeoff;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;

public class WriteOffFields extends FieldLayout<WriteOff> {

   private final DateField periodStart = new ADateField(WriteOff.class, "periodStart")
         .withRequired();
   private final DateField periodEnd = new ADateField(WriteOff.class, "periodEnd")
         .withRequired();

   private final OneToManyField<WriteOffItem> items = new OneToManyField<WriteOffItem>(
         WriteOffItem.class,
         ImmutableMap.of(
               "receiptItem", item -> item.getReceiptItem().getProduct().getName(),
               "productGroup", item -> item.getReceiptItem().getProduct().getProductGroup().getName()
         ),
         "productGroup", "receiptItem", "quantity", "unitPrice", "cost"
   ) {
      @Override
      protected Component createControls() {
         return new CssLayout();
      }
   };

   WriteOffFields(WriteOffService writeOffService) {
      periodStart.setRequiredIndicatorVisible(true);
      periodEnd.setRequiredIndicatorVisible(true);

      HasValue.ValueChangeListener<LocalDate> previewListener = event -> {
         LocalDate periodStartValue = periodStart.getValue();
         LocalDate periodEndValue = periodEnd.getValue();
         if (periodStartValue != null && periodEndValue != null) {
            WriteOff writeOffPreview = writeOffService.preview(periodStartValue, periodEndValue);
            items.setValue(writeOffPreview.getItems());
         }
      };

      periodStart.addValueChangeListener(previewListener);
      periodEnd.addValueChangeListener(previewListener);

      addComponents(new HorizontalLayout(periodStart, periodEnd), items);
   }

   @Override
   public void manualBinding(Binder<WriteOff> binder) {

      binder.forField(items)
            .withValidator(CollectionUtils::isNotEmpty, ctx -> App.translate(getClass(), "validation.items.empty"))
            .bind(writeOff -> {
                     if (writeOff.getId() == null) {
                        periodStart.setReadOnly(false);
                        periodEnd.setReadOnly(false);
                        return Collections.emptyList();
                     } else {
                        periodStart.setReadOnly(true);
                        periodEnd.setReadOnly(true);
                        items.setReadOnly(true);
                        return writeOff.getItems();
                     }
                  },
                  (writeOff, writeOffItems) -> writeOff.getItems()
                        .addAll(writeOffItems)
            );
   }
}
