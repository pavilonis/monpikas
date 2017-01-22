package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.components.ADateField;
import org.joda.time.LocalDateTime;

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
   protected List<Field> getFields() {
      return Arrays.asList(
            periodStart = new ADateField(getClass(), "periodStart").withRequired(true),
            periodEnd = new ADateField(getClass(), "periodEnd"),
            textField = new TextField(messages.get(this, "name"))
      );
   }

   @Override
   protected void setDefaultValues() {
      periodStart.setValue(
            LocalDateTime.now()
                  .minusWeeks(2)
                  .withTime(0, 0, 0, 0)
                  .toDate()
      );
   }

   @Override
   protected Field getFieldToFocus() {
      return textField;
   }
}