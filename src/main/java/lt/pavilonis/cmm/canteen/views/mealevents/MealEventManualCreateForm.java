package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.views.components.MealTypeComboBox;

import java.util.Date;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;

public class MealEventManualCreateForm extends Window {

   private final Button save = new Button("Pridėti pasirinktą", FontAwesome.PLUS);
   private final Button close = new Button("Uždaryti", FontAwesome.TIMES);
   private final BeanContainer<String, Pupil> container = new BeanContainer<>(Pupil.class);
   private final Table table = new PupilsTable("Pasirinkite mokinį", container);
   private final DateField dateField = new DateField("Pasirinkite data: ", new Date());
   private final ComboBox eventTypeCombo = new MealTypeComboBox();

   public MealEventManualCreateForm() {

      //TODO set form model (container) instead of getting values from fields manually

      setCaption("Rankinis maitinimosi įrašo įvėdimas");
      setResizable(false);
      setWidth("550px");
      setHeight("580px");

      HorizontalLayout hl = new HorizontalLayout(dateField, eventTypeCombo);
      hl.setSpacing(true);

      dateField.setDateFormat("yyyy-MM-dd");
      VerticalLayout vl = new VerticalLayout(hl, table);
      vl.setSpacing(true);
      vl.setMargin(true);

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      vl.addComponent(buttons);
      vl.setComponentAlignment(buttons, BOTTOM_CENTER);
      setContent(vl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public BeanContainer<String, Pupil> getContainer() {
      return container;
   }

   public Table getTable() {
      return table;
   }

   public Date getDate() {
      return dateField.getValue();
   }

   public MealType getEventType() {
      return (MealType) eventTypeCombo.getValue();
   }

   private class PupilsTable extends Table {
      public PupilsTable(String caption, BeanContainer<String, Pupil> container) {
         super(caption, container);
         container.setBeanIdProperty("cardCode");
         setSizeFull();
         setVisibleColumns(new String[]{"firstName", "lastName", "grade"});
         setColumnHeaders("Vardas", "Pavardė", "Klasė");
         setColumnWidth("grade", 85);
         setColumnCollapsingAllowed(true);
         setSelectable(true);
         setNullSelectionAllowed(false);
         setCacheRate(5);
         setHeight("340px");
      }
   }
}
