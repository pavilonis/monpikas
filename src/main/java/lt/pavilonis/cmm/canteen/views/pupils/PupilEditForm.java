package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.PupilLocalData;
import lt.pavilonis.cmm.canteen.views.components.PupilTypeComboBox;
import lt.pavilonis.cmm.canteen.views.pupils.form.MealTable;
import lt.pavilonis.cmm.canteen.views.settings.TableControlPanel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

import static com.vaadin.server.VaadinService.getCurrent;
import static com.vaadin.shared.ui.label.ContentMode.HTML;
import static com.vaadin.ui.Alignment.BOTTOM_CENTER;
import static com.vaadin.ui.Alignment.BOTTOM_RIGHT;
import static com.vaadin.ui.Button.ClickListener;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Objects.isNull;
import static lt.pavilonis.cmm.util.Messages.label;
import static org.slf4j.LoggerFactory.getLogger;

@UIScope
@SpringComponent
public class PupilEditForm extends Window {

   private static final Logger LOG = getLogger(PupilEditForm.class.getSimpleName());
   private final static Format DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static final String NO_PHOTO_PATH = "static/images/noPhoto.png";
   private final Button save = new Button("Saugoti");
   private final Button close = new Button("Uždaryti");
   private final BeanFieldGroup<PupilLocalData> group = new BeanFieldGroup<>(PupilLocalData.class);
   private final TableControlPanel controlPanel = new TableControlPanel();
   private final BeanContainer<Long, Meal> container = new BeanContainer<>(Meal.class);

   @Autowired
   public PupilEditForm(MessageSourceAdapter messages) {

      group.setItemDataSource(pupilData);
      group.setBuffered(true);

      MTable<Meal> table = new MealTable(Collections.<Meal>emptyList())
            .withCaption(messages.get(this, "portionsTable.caption"));
      table.setHeight("270px");
      table.setWidth("400px");
      container.addAll(pupilData.getMeals());

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

      HorizontalLayout buttons = new MHorizontalLayout(save, close)
            .withMargin(new MarginInfo(true, false, false, false));

      Image image = image(user.photoUrl);
      setContent(
            new HorizontalLayout(
                  new MVerticalLayout(name, date, last, table, controlPanel, buttons)
                        .withWidth("450px")
                        .withAlign(buttons, BOTTOM_RIGHT),
                  new MVerticalLayout(image, card, typeCombo, comment)
                        .withAlign(image, BOTTOM_CENTER))
      );
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

   public void edit(String cardCode) {
      pupilDataRepository.load(cardCode).orElse(new PupilLocalData(cardCode)),
            userRepository.load(cardCode).get(),
            mealService.lastMealEvent(cardCode)
   }
}
