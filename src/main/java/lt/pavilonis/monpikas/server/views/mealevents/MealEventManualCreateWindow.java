package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.views.converters.StringToBooleanCellConverter;

import java.util.Date;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Button.ClickListener;
import static com.vaadin.ui.Table.Align.CENTER;

public class MealEventManualCreateWindow extends Window {

   Button save = new Button("Pridėti pasirinktą", FontAwesome.PLUS);
   Button close = new Button("Uždaryti", FontAwesome.TIMES);
   BeanContainer<Long, AdbPupilDto> container = new BeanContainer<>(AdbPupilDto.class);
   Table table = new Table("Pasirinkite mokinį");
   DateField dateField = new DateField("Pasirinkite data: ", new Date());

   public MealEventManualCreateWindow() {

      setCaption("Rankinis maitinimosi įrašo įvėdimas");
      setResizable(false);
      setWidth("550px");
      setHeight("580px");
      VerticalLayout vl = new VerticalLayout();
      vl.setSpacing(true);
      vl.setMargin(true);
      container.setBeanIdProperty("cardId");

      table.setSizeFull();
      table.setContainerDataSource(container);
      table.setColumnHeader("firstName", "Vardas");
      table.setColumnHeader("lastName", "Pavardė");
      table.setColumnHeader("dinnerPermitted", "Pietus");
      table.setColumnHeader("breakfastPermitted", "Pusryčiai");
      table.setVisibleColumns(new String[]{"firstName", "lastName", "breakfastPermitted", "dinnerPermitted"});
      table.setColumnWidth("dinnerPermitted", 85);
      table.setColumnWidth("breakfastPermitted", 85);
      table.setConverter("dinnerPermitted", new StringToBooleanCellConverter());
      table.setConverter("breakfastPermitted", new StringToBooleanCellConverter());
      table.setColumnAlignment("dinnerPermitted", CENTER);
      table.setColumnAlignment("breakfastPermitted", CENTER);
      table.setColumnCollapsingAllowed(true);
      table.setSelectable(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);
      table.setHeight("340px");

      dateField.setDateFormat("yyyy-MM-dd");
      vl.addComponents(dateField, table);

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));
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

   public BeanContainer<Long, AdbPupilDto> getContainer() {
      return container;
   }

   public Table getTable() {
      return table;
   }

   public DateField getDateField() {
      return dateField;
   }
}
