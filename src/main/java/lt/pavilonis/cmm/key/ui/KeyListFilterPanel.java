package lt.pavilonis.cmm.key.ui;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.key.domain.Scanner;
import lt.pavilonis.cmm.key.repository.ScannerRestRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
class KeyListFilterPanel extends FilterPanel<KeyListFilter> {

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
   public KeyListFilter getFilter() {
      return new KeyListFilter(
            periodStart.getValue(),
            periodEnd.getValue(),
            scannerCombo.getValue() == null ? null : ((Scanner) scannerCombo.getValue()).getId(),
            textField.getValue(),
            isLogMode()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {

      List<HasValue<?>> fields = Arrays.asList(
            periodStart = new ADateField(this.getClass(), "periodStart").withRequired(),
            periodEnd = new ADateField(this.getClass(), "periodEnd").withRequired(),
            scannerCombo = scannerCombo(),
            textField = new ATextField(this.getClass(), "keyNumber"),
            activeKeysCheckBox = new ACheckBox(this.getClass(), "active")
      );

      activeKeysCheckBox.addValueChangeListener(value -> togglePeriodStartEnd());

      togglePeriodStartEnd();
      return fields;
   }

   private ComboBox scannerCombo() {
      ScannerRestRepository scannerRepo = App.context.getBean(ScannerRestRepository.class);
      ComboBox<Scanner> combo = new ComboBox<>(
            messages.get(this, "scanner"),
            scannerRepo.loadAll()
      );
      combo.setItemCaptionGenerator(item -> messages.get(item, item.getName()));
      return combo;
   }

   @Override
   protected void setDefaultValues() {
      periodStart.setValue(LocalDate.now().minusWeeks(1));
      periodEnd.setValue(LocalDate.now());
   }

   @Override
   public void addSearchClickListener(Button.ClickListener clickListener) {
      super.addSearchClickListener(clickListener);
      activeKeysCheckBox.addValueChangeListener(
            change -> clickListener.buttonClick(new ClickEvent(activeKeysCheckBox))
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
