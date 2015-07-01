package lt.pavilonis.monpikas.server.views.pupils;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import lt.pavilonis.monpikas.server.views.converters.MealTypeCellConverter;
import lt.pavilonis.monpikas.server.views.settings.TableControlPanel;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Alignment.BOTTOM_RIGHT;
import static com.vaadin.ui.Button.ClickListener;
import static lt.pavilonis.monpikas.server.utils.Messages.label;

public class PupilEditWindow extends Window {

   private final static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

   @PropertyId("comment")
   private final Button save = new Button("Saugoti");
   private final Button close = new Button("Uždaryti");
   private final BeanFieldGroup<Pupil> group = new BeanFieldGroup<>(Pupil.class);
   private final TableControlPanel controlPanel = new TableControlPanel();
   private final BeanContainer<Long, Meal> container = new BeanContainer<>(Meal.class);
   private final Table table = new Table(label("PupilEditWindow.PortionsTable.Caption"), container);

   public PupilEditWindow(Pupil pupil, PupilDto dto, Image image, Optional<Date> lastMealDate) {

      group.setItemDataSource(pupil);
      group.setBuffered(true);

      //table.setSizeFull();
      table.setHeight("270px");
      table.setWidth("400px");
      container.setBeanIdProperty("id");
      container.addAll(pupil.getMeals());
      container.sort(new Object[]{"id"}, new boolean[]{true});
      table.setVisibleColumns("id", "name", "type", "price");
      table.setColumnHeaders("Id", "Pavadinimas", "Tipas", "Kaina");

      table.setConverter("type", new MealTypeCellConverter());
      table.setConverter("price", new StringToDoubleConverter() {
         @Override
         protected NumberFormat getFormat(Locale locale) {
            return new DecimalFormat("0.00");
         }
      });

      table.setColumnCollapsingAllowed(true);
      table.setColumnCollapsed("id", true);
      table.setSelectable(true);
      table.setMultiSelect(true);
      table.setNullSelectionAllowed(false);
      table.setCacheRate(5);

      setCaption("Mokinio nustatymai");
      setResizable(false);
      setWidth("655px");
      setHeight("650px");

      String birthDate = dto.getBirthDate().map(LocalDate::toString).orElse("nenurodyta");
      String mealDate = lastMealDate.map(DATE_FORMAT::format).orElse("nėra duomenų");

      Label last = new Label(label("PupilEditWindow.Label.LastMeal") + mealDate, HTML);
      Label card = new Label("<b>Kortelės #:</b> " + dto.getCardId(), HTML);
      Label name = new Label("<b>Vardas:</b> " + dto.getFirstName() + " " + dto.getLastName(), HTML);
      Label date = new Label("<b>Gimimo data:</b> " + birthDate, HTML);

      TextField grade = group.buildAndBind("Klasė", "grade", TextField.class);
      grade.setNullRepresentation("");

      TextArea comment = group.buildAndBind("Komentaras", "comment", TextArea.class);
      comment.setNullRepresentation("");

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));

      VerticalLayout leftColumn = new VerticalLayout(name, date, last, table, controlPanel, buttons);
      //vl1.setExpandRatio(table, 1f);
      leftColumn.setSpacing(true);
      leftColumn.setWidth("450px");
      leftColumn.setMargin(true);
      leftColumn.setComponentAlignment(buttons, BOTTOM_RIGHT);

      image.setWidth("170px");
      image.setHeight("211px");

      VerticalLayout rightColumn = new VerticalLayout(image, card, grade, comment);
      rightColumn.setComponentAlignment(image, BOTTOM_CENTER);
      rightColumn.setSpacing(true);

      //grade.setWidth("220px");
      //name.setWidth("220px");

      setContent(new HorizontalLayout(leftColumn, rightColumn));
      setModal(true);
   }

   public void addSaveButtonListener(ClickListener listener) {
      save.addClickListener(listener);
   }

   public void addCloseButtonListener(ClickListener listener) {
      close.addClickListener(listener);
   }

   public void addAddMealButtonListener(ClickListener listener) {
      controlPanel.addAddListener(listener);
   }

   public void addRemoveMealButtonListener(ClickListener listener) {
      controlPanel.addDeleteListener(listener);
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

   public void removeFromContainer(Collection<Long> ids) {
      ids.forEach(container::removeItem);
   }

   public void addToContainer(Collection<Meal> meals) {
      container.addAll(meals);
   }

   public Table getTable() {
      return table;
   }
}
