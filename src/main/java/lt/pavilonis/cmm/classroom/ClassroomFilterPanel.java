package lt.pavilonis.cmm.classroom;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

final class ClassroomFilterPanel extends FilterPanel<ClassroomFilter> {

   private TextField textField;
   private CheckBox checkBoxHistory;
   private DateField periodStart;
   private DateField periodEnd;

   private void togglePeriodStartEnd() {
      boolean mode = isHistoryMode();
      periodStart.setEnabled(mode);
      periodEnd.setEnabled(mode);
   }

   boolean isHistoryMode() {
      return checkBoxHistory.getValue() == Boolean.TRUE;
   }

   @Override
   public ClassroomFilter getFilter() {
      return new ClassroomFilter(
            periodStart.getValue(),
            periodEnd.getValue(),
            textField.getValue(),
            isHistoryMode()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {

      List<HasValue<?>> fields = Arrays.asList(
            periodStart = new ADateField(this.getClass(), "periodStart").withRequired(),
            periodEnd = new ADateField(this.getClass(), "periodEnd").withRequired(),
            textField = new ATextField(this.getClass(), "classroomNumber"),
            checkBoxHistory = new ACheckBox(this.getClass(), "log")
      );

      checkBoxHistory.addValueChangeListener(value -> togglePeriodStartEnd());

      togglePeriodStartEnd();
      return fields;
   }

   @Override
   protected void setDefaultValues() {
      periodStart.setValue(LocalDate.now().minusWeeks(1));
      periodEnd.setValue(LocalDate.now());
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
      return textField;
   }
}
