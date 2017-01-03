package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.PupilLocalData;
import lt.pavilonis.cmm.canteen.domain.UserRepresentation;
import lt.pavilonis.cmm.canteen.views.components.PupilTypeComboBox;
import lt.pavilonis.cmm.canteen.views.converters.MealTypeCellConverter;
import lt.pavilonis.cmm.canteen.views.settings.TableControlPanel;
import org.slf4j.Logger;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static com.vaadin.server.VaadinService.getCurrent;
import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Alignment.BOTTOM_RIGHT;
import static com.vaadin.ui.Button.ClickListener;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Objects.isNull;
import static lt.pavilonis.cmm.util.Messages.label;
import static org.slf4j.LoggerFactory.getLogger;

public class PupilEditWindow extends Window {

   private static final Logger LOG = getLogger(PupilEditWindow.class.getSimpleName());
   private final static Format DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static final String NO_PHOTO_PATH = "static/images/noPhoto.png";
   private final Button save = new Button("Saugoti");
   private final Button close = new Button("Uždaryti");
   private final BeanFieldGroup<PupilLocalData> group = new BeanFieldGroup<>(PupilLocalData.class);
   private final TableControlPanel controlPanel = new TableControlPanel();
   private final BeanContainer<Long, Meal> container = new BeanContainer<>(Meal.class);
   private final Table table = new Table(label("PupilEditWindow.PortionsTable.Caption"), container);

   public PupilEditWindow(PupilLocalData pupilData, UserRepresentation user, Optional<Date> lastMealDate) {

      group.setItemDataSource(pupilData);
      group.setBuffered(true);

      table.setHeight("270px");
      table.setWidth("400px");
      container.setBeanIdProperty("id");
      container.addAll(pupilData.getMeals());
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

      String birthDate = isNull(user.birthDate)
            ? "nenurodyta"
            : DateTimeFormatter.ISO_LOCAL_DATE.format(user.birthDate);

      String mealDate = lastMealDate.map(DATE_TIME_FORMAT::format).orElse("nėra duomenų");

      Label last = new Label(label("PupilEditWindow.Label.LastMeal") + mealDate, HTML);
      Label card = new Label("<b>Kortelės #:</b> " + pupilData.getCardCode(), HTML);
      Label name = new Label("<b>Vardas:</b> " + user.firstName + " " + user.lastName, HTML);
      Label date = new Label("<b>Gimimo data:</b> " + birthDate, HTML);

      ComboBox typeCombo = new PupilTypeComboBox();
      typeCombo.setRequired(true);
      group.bind(typeCombo, "type");
      typeCombo.setNullSelectionAllowed(false);

      TextArea comment = group.buildAndBind("Komentaras", "comment", TextArea.class);
      comment.setNullRepresentation("");

      HorizontalLayout buttons = new HorizontalLayout(save, close);
      buttons.setSpacing(true);
      buttons.setMargin(new MarginInfo(true, false, false, false));

      VerticalLayout leftColumn = new VerticalLayout(name, date, last, table, controlPanel, buttons);
      leftColumn.setSpacing(true);
      leftColumn.setWidth("450px");
      leftColumn.setMargin(true);
      leftColumn.setComponentAlignment(buttons, BOTTOM_RIGHT);

      Image image = image(user.photoUrl);
      VerticalLayout rightColumn = new VerticalLayout(image, card, typeCombo, comment);
      rightColumn.setComponentAlignment(image, BOTTOM_CENTER);
      rightColumn.setSpacing(true);

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

   private Image image(String url) {
      Resource resource = remoteImageExists(url)
            ? new ExternalResource(url)
            : new FileResource(new File(
            //TODO simplify?
            getCurrent().getBaseDirectory().getAbsolutePath() + File.separator + NO_PHOTO_PATH));

      Image image = new Image(null, resource);
      image.setWidth("170px");
      image.setHeight("211px");
      return image;
   }

   private boolean remoteImageExists(String url) {
      try {
         URL u = new URL(url);
         u.getPath();
         HttpURLConnection http = (HttpURLConnection) u.openConnection();
         http.setInstanceFollowRedirects(false);
         http.setRequestMethod("HEAD");
         http.connect();
         return (http.getResponseCode() == HTTP_OK);
      } catch (Exception e) {
         LOG.error("Image request error: " + e);
         return false;
      }
   }

   public PupilLocalData getModel() {
      return group.getItemDataSource().getBean();
   }
}