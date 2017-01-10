package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;
import java.util.List;

import static com.vaadin.ui.Button.ClickListener;

public class MealEventManualCreateForm extends Window {

   private final Button save = new Button("Pridėti pasirinktą", FontAwesome.PLUS);
   private final Button close = new Button("Uždaryti", FontAwesome.TIMES);
   private final Table table = new UserMealTable("Pasirinkite mokinį");
   private final DateField dateField = new DateField("Pasirinkite data: ", new Date());
   private final ComboBox eventTypeCombo;

   public MealEventManualCreateForm(List<UserMeal> userMeals) {

      //TODO set form model (container) instead of getting values from fields manually

      setCaption("Rankinis maitinimosi įrašo įvėdimas");
      setResizable(false);
      setWidth("550px");
      setHeight("580px");

      dateField.setDateFormat("yyyy-MM-dd");
      VerticalLayout vl = new MVerticalLayout(
            new MHorizontalLayout(
                  dateField,
                  eventTypeCombo = new EnumComboBox<>(MealType.class)
            ),
            table,
            new MHorizontalLayout(save, close)
      );

//      vl.addComponent(buttons);
//      vl.setComponentAlignment(buttons, BOTTOM_CENTER);
      setContent(vl);
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
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

   private class UserMealTable extends MTable<UserMeal> {
      public UserMealTable(String caption) {
         setCaption(caption);
         setSizeFull();
         setVisibleColumns("firstName", "lastName", "grade");
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
