package lt.pavilonis.monpikas.server.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.views.converters.OptionalBooleanCellConverter;
import lt.pavilonis.monpikas.server.views.converters.OptionalCellConverter;

import java.util.Date;

import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Alignment.MIDDLE_RIGHT;
import static com.vaadin.ui.Button.ClickListener;
import static com.vaadin.ui.Table.Align.CENTER;
import static java.util.Arrays.asList;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;

public class MealEventManualCreateWindow extends Window {

   Button save = new Button("Pridėti pasirinktą", FontAwesome.PLUS);
   Button close = new Button("Uždaryti", FontAwesome.TIMES);
   BeanContainer<Long, AdbPupilDto> container = new BeanContainer<>(AdbPupilDto.class);
   Table table = new Table("Pasirinkite mokinį");
   DateField dateField = new DateField("Pasirinkite data: ", new Date());
   OptionGroup portionType = new OptionGroup("Maitinimosi tipas", asList(PortionType.values()));

   public MealEventManualCreateWindow() {

      //TODO set form model (container) instead of getting values from fields manually

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
      table.setVisibleColumns(new String[]{"firstName", "lastName", "grade", "breakfastPortion", "dinnerPortion"});

      table.setColumnHeader("firstName", "Vardas");
      table.setColumnHeader("lastName", "Pavardė");
      table.setColumnHeader("breakfastPortion", "Pusryčiai");
      table.setColumnHeader("dinnerPortion", "Pietus");
      table.setColumnHeader("grade", "Klasė");
      table.setColumnWidth("breakfastPortion", 85);
      table.setColumnWidth("dinnerPortion", 85);
      table.setColumnWidth("grade", 85);

      table.setConverter("breakfastPortion", new OptionalBooleanCellConverter());
      table.setConverter("dinnerPortion", new OptionalBooleanCellConverter());
      table.setConverter("grade", new OptionalCellConverter());
      table.setColumnAlignment("breakfastPortion", CENTER);
      table.setColumnAlignment("dinnerPortion", CENTER);
      table.setColumnCollapsingAllowed(true);
      table.setSelectable(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);
      table.setHeight("340px");

      portionType.setNullSelectionAllowed(false);
      portionType.select(DINNER);
      portionType.setItemCaption(BREAKFAST, "Pusryčiai");
      portionType.setItemCaption(DINNER, "Pietus");
      HorizontalLayout hl = new HorizontalLayout(dateField, new Label(" "), portionType);
      hl.setSpacing(true);
      hl.setComponentAlignment(portionType, MIDDLE_RIGHT);

      dateField.setDateFormat("yyyy-MM-dd");
      vl.addComponents(
            hl,
            table
      );

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

   public BeanContainer<Long, AdbPupilDto> getContainer() {
      return container;
   }

   public Table getTable() {
      return table;
   }

   public Date getDate() {
      return dateField.getValue();
   }

   public PortionType getPortionType() {
      return (PortionType) portionType.getValue();
   }
}
