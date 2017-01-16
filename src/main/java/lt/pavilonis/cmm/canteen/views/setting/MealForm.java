package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.converter.LocalTimeToDateConverter;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MFormLayout;

import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

@SpringComponent
@UIScope
public class MealForm extends AbstractFormController<Meal, Long> {

   private static final Converter<Date, LocalTime> TIME_CONVERTER = new LocalTimeToDateConverter();
   private final MDateField startTime = new MDateField("Periodo pradžia: ")
         .withResolution(Resolution.MINUTE)
         .withStyleName("time-only");
   private final MDateField endTime = new MDateField("Periodo pabaiga: ")
         .withResolution(Resolution.MINUTE)
         .withStyleName("time-only");
   private final MTextField price = new MTextField("Kaina")
         .withNullRepresentation("");
   private final MTextField name = new MTextField("Pavadinimas")
         .withNullRepresentation("");
   private final ComboBox type = new EnumComboBox<>(MealType.class);

   @Autowired
   private MealRepository mealRepository;

   @Override
   protected EntityRepository<Meal, Long> getEntityRepository() {
      return mealRepository;
   }

   public MealForm() {
      startTime.setLocale(new Locale("lt", "LT"));
      startTime.setConverter(TIME_CONVERTER);
      endTime.setLocale(new Locale("lt", "LT"));
      endTime.setConverter(TIME_CONVERTER);
   }

   @Override
   protected Component createFieldLayout() {
      return new MFormLayout(name, type, price, startTime, endTime);
   }
}
