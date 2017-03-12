package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@UIScope
@SpringComponent
public class MealEventFilterPanel extends FilterPanel<MealEventFilter> {

   private TextField textField;
   private DateField periodStart;
   private DateField periodEnd;

   @Override
   public MealEventFilter getFilter() {
      return new MealEventFilter(
            textField.getValue(),
            periodStart.getValue(),
            periodEnd.getValue()
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