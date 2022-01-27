package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.Named;
import lt.pavilonis.monpikas.common.field.ATextField;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.monpikas.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.monpikas.scanner.Scanner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class ScanLogBriefFilterPanel extends PeriodFilterPanel<ScanLogBriefFilter> {

   private TextField textField;
   private ComboBox<Scanner> scannerCombo;
   private ComboBox<String> roleCombo;

   public ScanLogBriefFilterPanel(List<String> roles, List<Scanner> scanners) {
      scannerCombo.setItems(scanners);
      scannerCombo.setItemCaptionGenerator(Named::getName);
      roleCombo.setItems(roles);
   }

   @Override
   public ScanLogBriefFilter getFilter() {
      IdPeriodFilter filter = super.getFilter();

      return ScanLogBriefFilter.builder()
            .periodStart(filter.getPeriodStart())
            .periodEnd(filter.getPeriodEnd())
            .text(textField.getValue())
            .scannerId(scannerCombo.getValue() == null ? null : scannerCombo.getValue().getId())
            .role(roleCombo.getValue())
            .build();
   }

   @Override
   protected List<HasValue<?>> getFields() {
      var result = new ArrayList<>(super.getFields());
      result.add(textField = new ATextField(getClass(), "text"));
      result.add(scannerCombo = new ComboBox<>(App.translate(getClass(), "scanner")));
      result.add(roleCombo = new ComboBox<>(App.translate(getClass(), "role")));

      result.forEach(field -> {
         if (field instanceof AbstractComponent) {
            ((AbstractComponent) field).setWidth(140, Unit.PIXELS);
         }
      });

      return result;
   }

   @Override
   protected void setDefaultValues() {
      getPeriodStart().setValue(LocalDate.now());
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
