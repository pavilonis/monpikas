package lt.pavilonis.monpikas.key.ui;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.field.ACheckBox;
import lt.pavilonis.monpikas.common.field.ATextField;
import lt.pavilonis.monpikas.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.monpikas.scanner.Scanner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class KeyListFilterPanel extends PeriodFilterPanel<KeyListFilter> {

   private TextField text;
   private ComboBox<Scanner> scannerCombo;
   private CheckBox checkBoxHistory;

   public KeyListFilterPanel(List<Scanner> scanners) {
      scannerCombo.setItems(scanners);
      scannerCombo.setItemCaptionGenerator(Scanner::getName);
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
      return KeyListFilter.builder()
            .periodStart(getPeriodStart().getValue())
            .periodEnd(getPeriodEnd().getValue())
            .id(scannerCombo.getValue() == null ? null : scannerCombo.getValue().getId())
            .text(text.getValue())
            .logMode(isHistoryMode())
            .build();
   }

   @Override
   protected List<HasValue<?>> getFields() {
      var result = new ArrayList<>(super.getFields());

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
