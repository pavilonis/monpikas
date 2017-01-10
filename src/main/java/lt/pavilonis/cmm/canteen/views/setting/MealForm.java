package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.canteen.views.converter.LocalTimeToDateConverter;

import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;

public class MealForm extends Window {

   private final static Converter<Date, LocalTime> TIME_CONVERTER = new LocalTimeToDateConverter();
   private final BeanFieldGroup<Meal> group = new BeanFieldGroup<>(Meal.class);
   private final Button save = new Button("Saugoti");
   private final Button close = new Button("Uždaryti");

   public MealForm(Item item) {
      this();
      group.setItemDataSource(item);
   }

   //TODO add period overlap validator

   public MealForm() {
      group.setItemDataSource(new Meal());
      setCaption("Porcijos tipas");
      setResizable(false);
      setWidth("350px");
      setHeight("380px");

      ComboBox type = new EnumComboBox<>(MealType.class);
      group.bind(type, "type");

      TextField field = (TextField) group.buildAndBind("Kaina", "price");
      field.setNullRepresentation("");

      InlineDateField startTime = new InlineDateField("Periodo pradžia: ");
      startTime.setResolution(Resolution.MINUTE);
      startTime.setLocale(new Locale("lt", "LT"));
      startTime.addStyleName("time-only");
      startTime.setConverter(TIME_CONVERTER);

      InlineDateField endTime = new InlineDateField("Periodo pabaiga: ");
      endTime.setResolution(Resolution.MINUTE);
      endTime.setLocale(new Locale("lt", "LT"));
      endTime.addStyleName("time-only");
      endTime.setConverter(TIME_CONVERTER);

      group.bind(startTime, "startTime");
      group.bind(endTime, "endTime");

      FormLayout fl = new FormLayout(group.buildAndBind("Pavadinimas", "name"), type, field, startTime, endTime);

      ((TextField) group.getField("name")).setNullRepresentation("");
      group.setBuffered(true);
      fl.setSpacing(true);
      fl.setMargin(true);

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
      fl.addComponent(buttons);
      fl.setComponentAlignment(buttons, BOTTOM_CENTER);

      setContent(fl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public BeanItem<Meal> getItemDateSource() {
      return group.getItemDataSource();
   }

   public void commit() {
      try {
         group.commit();
      } catch (FieldGroup.CommitException e) {
         e.printStackTrace();
      }
   }

   public boolean isValid() {
      return group.isValid();
   }
}
