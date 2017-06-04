package lt.pavilonis.cmm.school.classroom;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class ClassroomFilterPanel extends PeriodFilterPanel<ClassroomFilter> {

   private TextField textField;
   private CheckBox checkBoxHistory;

   private void togglePeriodStartEnd() {
      boolean mode = isHistoryMode();
      getPeriodStart().setEnabled(mode);
      getPeriodEnd().setEnabled(mode);
   }

   boolean isHistoryMode() {
      return checkBoxHistory.getValue() == Boolean.TRUE;
   }

   @Override
   public ClassroomFilter getFilter() {
      return new ClassroomFilter(
            getPeriodStart().getValue(),
            getPeriodStart().getValue(),
            textField.getValue(),
            isHistoryMode()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {

      List<HasValue<?>> result = new ArrayList<>(super.getFields());
      result.add(textField = new ATextField(this.getClass(), "classroomNumber"));
      result.add(checkBoxHistory = new ACheckBox(this.getClass(), "log"));

      checkBoxHistory.addValueChangeListener(value -> togglePeriodStartEnd());

      togglePeriodStartEnd();
      return result;
   }

   @Override
   protected void setDefaultValues() {
      getPeriodStart().setValue(LocalDate.now().minusDays(2));
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
