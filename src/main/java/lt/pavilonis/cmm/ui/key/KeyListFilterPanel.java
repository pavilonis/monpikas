package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.components.ADateField;
import lt.pavilonis.cmm.converter.LocalDateConverter;
import lt.pavilonis.cmm.domain.ScannerRepresentation;
import lt.pavilonis.cmm.repository.ScannerRestRepository;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.fields.MTextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringComponent
@UIScope
class KeyListFilterPanel extends FilterPanel<KeyFilter> {

   private TextField textField;
   private ComboBox scannerCombo;
   private CheckBox activeKeysCheckBox;
   private DateField periodStart;
   private DateField periodEnd;

   private void togglePeriodStartEnd() {
      boolean mode = isLogMode();
      periodStart.setEnabled(mode);
      periodEnd.setEnabled(mode);
   }

   boolean isLogMode() {
      return activeKeysCheckBox.getValue() != Boolean.TRUE;
   }

   @Override
   public KeyFilter getFilter() {
      return new KeyFilter(
            (LocalDate) periodStart.getConvertedValue(),
            (LocalDate) periodEnd.getConvertedValue(),
            scannerCombo.getValue() == null ? null : ((ScannerRepresentation) scannerCombo.getValue()).getId(),
            textField.getValue(),
            isLogMode()
      );
   }

   @Override
   protected List<Field> getFields() {
      List<Field> fields = Arrays.asList(
            periodStart = new ADateField(this.getClass(), "periodStart")
                  .withRequired(true)
                  .withConverter(new LocalDateConverter()),
            periodEnd = new ADateField(this.getClass(), "periodEnd")
                  .withRequired(true)
                  .withConverter(new LocalDateConverter()),
            scannerCombo = scannerCombo(),
            textField = new MTextField(messages.get(this, "keyNumber")),
            activeKeysCheckBox = new MCheckBox(messages.get(this, "active"), true)
                  .withValueChangeListener(value -> togglePeriodStartEnd())
      );

      activeKeysCheckBox.setImmediate(true);
      togglePeriodStartEnd();
      return fields;
   }

   private ComboBox scannerCombo() {
      ScannerRestRepository scannerRepo = App.context.getBean(ScannerRestRepository.class);
      return new ComboBox(messages.get(this, "scanner"), scannerRepo.loadAll()) {
         @Override
         public String getItemCaption(Object itemId) {
            ScannerRepresentation scanner = (ScannerRepresentation) itemId;
            return messages.get(scanner, scanner.getName());
         }
      };
   }

   @Override
   protected void setDefaultValues() {
      periodStart.setConvertedValue(LocalDate.now().minusWeeks(1));
      periodEnd.setValue(new Date());
   }

   @Override
   public void addSearchClickListener(Button.ClickListener clickListener) {
      super.addSearchClickListener(clickListener);
      activeKeysCheckBox.addValueChangeListener(
            change -> clickListener.buttonClick(new ClickEvent(activeKeysCheckBox))
      );
   }


   //   if (!periodStart.isEmpty() && !periodEnd.isEmpty()) {
//      reload();
//   } else {
//      Notification.show(
//            messages.get(this, "emptyFilterDates"),
//            Notification.Type.WARNING_MESSAGE
//      );
//   }


   @Override
   protected Field getFieldToFocus() {
      return textField;
   }
}
