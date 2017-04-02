package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.data.Binder;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateTimeField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;

public class MealFormView extends FormView<Meal> {

   private final InlineDateTimeField timeStartField = dateField("timePeriodStart");
   private final InlineDateTimeField timeEndField = dateField("timePeriodEnd");
   private final TextField priceField = new ATextField(getClass(), "price").withRequired();
   private final TextField name = new ATextField(getClass(), "name").withRequired();

   private final ComboBox<MealType> type = new EnumComboBox<>(MealType.class)
         .withRequired(true);

   public MealFormView() {
      addComponents(
            new HorizontalLayout(name, type),
            new HorizontalLayout(timeStartField, timeEndField),
            priceField
      );
   }

   private InlineDateTimeField dateField(String captionCode) {
      InlineDateTimeField dateTimeField = new InlineDateTimeField();
      dateTimeField.setResolution(DateTimeResolution.MINUTE);
      dateTimeField.setLocale(new Locale("lt", "LT"));
      dateTimeField.addStyleName("time-only");
      dateTimeField.setCaption(App.translate(this, captionCode));
      dateTimeField.setRequiredIndicatorVisible(true);
      return dateTimeField;
   }

   /**
    * {@link InlineDateTimeField} uses {@link java.time.LocalDateTime} as
    * data model, while {@link Meal} bean uses {@link java.time.LocalTime}.
    * Binding with custom setters/getters to convert date types.
    */
   @Override
   public void manualBinding(Binder<Meal> binding) {
      binding.bind(
            timeStartField,
            meal -> extractDateTime(meal.getStartTime(), LocalTime.MIN),
            (meal, value) -> meal.setStartTime(value.toLocalTime())
      );

      binding.bind(
            timeEndField,
            meal -> extractDateTime(meal.getEndTime(), LocalTime.MAX),
            (meal, value) -> meal.setEndTime(value.toLocalTime())
      );

      binding.bind(
            priceField,
            meal -> meal.getPrice() == null ? null : String.valueOf(meal.getPrice()),
            (meal, value) -> meal.setPrice(new BigDecimal(value))
      );
   }

   protected LocalDateTime extractDateTime(LocalTime localTime, LocalTime defaultValue) {
      return Optional.ofNullable(localTime)
            .map(time -> time.atDate(LocalDate.MIN))
            .orElse(defaultValue.atDate(LocalDate.MIN));
   }
}
