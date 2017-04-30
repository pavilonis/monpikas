package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EatingEventFilterPanel extends FilterPanel<EatingEventFilter> {

   private TextField textField;
   private DateField periodStart;
   private DateField periodEnd;

   @Override
   public EatingEventFilter getFilter() {
      return new EatingEventFilter(
            textField.getValue(),
            periodStart.getValue(),
            periodEnd.getValue(),
            null
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      return Arrays.asList(
            periodStart = new ADateField(getClass(), "periodStart").withRequired(),
            periodEnd = new ADateField(getClass(), "periodEnd"),
            textField = new ATextField(this.getClass(), "name")
      );
   }

   @Override
   protected void setDefaultValues() {
      periodStart.setValue(
            LocalDate.now().minusWeeks(2)
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}