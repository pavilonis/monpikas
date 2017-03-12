package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateTimeField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;

import java.util.Locale;

public class MealFormView extends FormView<Meal> {

   //   private static final Converter<Date, LocalTime> TIME_CONVERTER = new LocalTimeToDateConverter();
   private final InlineDateTimeField startTime = dateField("Periodo pradžia: ");
   private final InlineDateTimeField endTime = dateField("Periodo pabaiga: ");

   private final TextField price = new ATextField(this.getClass(), "price").withRequired();
   private final TextField name = new ATextField(this.getClass(), "name").withRequired();

   private final ComboBox type = new EnumComboBox<>(MealType.class)
         .withRequired(true);

   public MealFormView() {
//      super(2, 3);
      addComponents(
            new HorizontalLayout(name, type),
            new HorizontalLayout(startTime, endTime),
            price
      );
   }

   private InlineDateTimeField dateField(String caption) {
      InlineDateTimeField dateTimeField = new InlineDateTimeField();
      dateTimeField.setResolution(DateTimeResolution.MINUTE);
      dateTimeField.setLocale(new Locale("lt", "LT"));
      dateTimeField.addStyleName("time-only");
//      dateTimeField.setConverter(TIME_CONVERTER);
      dateTimeField.setCaption(caption);
      dateTimeField.setRequiredIndicatorVisible(true);
      return dateTimeField;
   }
}
