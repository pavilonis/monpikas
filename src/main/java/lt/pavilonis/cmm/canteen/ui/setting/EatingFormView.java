package lt.pavilonis.cmm.canteen.ui.setting;

import com.vaadin.data.Binder;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateTimeField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;

import static com.vaadin.data.ValidationResult.error;
import static com.vaadin.data.ValidationResult.ok;

public class EatingFormView extends FieldLayout<Eating> {

   private final InlineDateTimeField timeStartField = dateField("timePeriodStart");
   private final InlineDateTimeField timeEndField = dateField("timePeriodEnd");
   private final TextField priceField = new ATextField(getClass(), "price").withRequired();
   private final TextField name = new ATextField(getClass(), "name").withRequired();

   private final ComboBox<EatingType> type = new EnumComboBox<>(EatingType.class)
         .withRequired(true);

   public EatingFormView() {
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
    * data model, while {@link Eating} bean uses {@link java.time.LocalTime}.
    * Binding with custom setters/getters to convert date types.
    */
   @Override
   public void manualBinding(Binder<Eating> binding) {

//      binding.forField(name)
//            .withValidator(
//                  (value, context) -> value != null ? ok() : error("can not be null")
//            );

      binding.forField(timeStartField)
            .withValidator((value, context) -> value != null ? ok() : error("can not be null"))
            .bind(
                  eating -> extractDateTime(eating.getStartTime(), LocalTime.MIN),
                  (eating, value) -> eating.setStartTime(value.toLocalTime())
            );

      binding.forField(timeEndField)
            .withValidator((value, context) -> value != null ? ok() : error("can not be null"))
            .bind(
                  eating -> extractDateTime(eating.getEndTime(), LocalTime.MAX),
                  (eating, value) -> eating.setEndTime(value.toLocalTime())
            );

      binding.forField(priceField)
            .withValidator(
                  (value, context) -> NumberUtils.isParsable(value) ? ok() : error("bad number"))
            .bind(
                  eating -> eating.getPrice() == null ? null : String.valueOf(eating.getPrice()),
                  (eating, value) -> eating.setPrice(new BigDecimal(value))
            );
   }

   protected LocalDateTime extractDateTime(LocalTime localTime, LocalTime defaultValue) {
      return Optional.ofNullable(localTime)
            .map(time -> time.atDate(LocalDate.MIN))
            .orElse(defaultValue.atDate(LocalDate.MIN));
   }
}
