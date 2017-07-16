package lt.pavilonis.cmm.school.key.ui;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class KeyListFilterPanel extends PeriodFilterPanel<KeyListFilter> {

   private TextField text;
   private ComboBox<Scanner> scannerCombo;
   private CheckBox checkBoxHistory;

   public KeyListFilterPanel(List<Scanner> scanners) {
      scannerCombo.setItems(scanners);
      scannerCombo.setItemCaptionGenerator(item -> App.translate(item, item.getName()));
   }

   private void togglePeriodStartEnd() {
      boolean mode = isHistoryMode();
      getPeriodStart().setEnabled(mode);
      getPeriodEnd().setEnabled(mode);
   }

   boolean isHistoryMode() {
      return checkBoxHistory.getValue() == Boolean.TRUE;
   }

   @Override
   public KeyListFilter getFilter() {
      return new KeyListFilter(
            getPeriodStart().getValue(),
            getPeriodEnd().getValue(),
            scannerCombo.getValue() == null ? null : scannerCombo.getValue().getId(),
            text.getValue(),
            isHistoryMode()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> result = new ArrayList<>(super.getFields());

      result.add(scannerCombo = new ComboBox<>(App.translate(this, "scanner")));
      result.add(text = new ATextField(this.getClass(), "text"));
      result.add(checkBoxHistory = new ACheckBox(this.getClass(), "log"));

      checkBoxHistory.addValueChangeListener(value -> togglePeriodStartEnd());

      togglePeriodStartEnd();

      result.forEach(field -> {
         if (field instanceof AbstractComponent) {
            ((AbstractComponent) field).setWidth(140, Unit.PIXELS);
         }
      });
      checkBoxHistory.setWidthUndefined();
      return result;
   }

   @Override
   protected void setDefaultValues() {
      getPeriodStart().setValue(LocalDate.now().minusDays(1));
   }

   @Override
   public void addSearchClickListener(Button.ClickListener clickListener) {
      super.addSearchClickListener(clickListener);
      checkBoxHistory.addValueChangeListener(
            change -> clickListener.buttonClick(new ClickEvent(checkBoxHistory))
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return text;
   }
}
