package lt.pavilonis.cmm.canteen.views.user.form;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealData;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.canteen.views.setting.TableControlPanel;
import lt.pavilonis.cmm.users.service.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.slf4j.LoggerFactory.getLogger;

@UIScope
@SpringComponent
public class UserMealPopup extends Window {

   private static final Logger LOG = getLogger(UserMealPopup.class.getSimpleName());
   private final static Format DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static final String NO_PHOTO_PATH = "static/images/noPhoto.png";

   private final BeanFieldGroup<MealData> group = new BeanFieldGroup<>(MealData.class);
   private final MealTable mealTable = new MealTable(Collections.<Meal>emptyList());
   private final MTextField nameField;
   private final MTextField birthDateField;
   private final MTextField lastMealField;
   private final MTextField cardCodeField;
   private final MCssLayout photoWrapperLayout = new MCssLayout();

   private final UserMealService pupilService;
   private final MealService mealService;
   private final ImageService imageService;
   private final MessageSourceAdapter messages;

   @Autowired
   public UserMealPopup(MessageSourceAdapter messages, UserMealService pupilService,
                        MealService mealService, ImageService imageService,
                        MealRepository mealRepository) {
      this.messages = messages;
      this.pupilService = pupilService;
      this.mealService = mealService;
      this.imageService = imageService;

      this.setCaption("Mokinio nustatymai");
      this.setResizable(false);
      this.setWidth("655px");
      this.setHeight("650px");
      this.nameField = new MTextField(messages.get(this, "name"));
      this.birthDateField = new MTextField(messages.get(this, "birthDate"));
      this.lastMealField = new MTextField(messages.get(this, "lastMeal"));
      this.cardCodeField = new MTextField(messages.get(this, "cardCode"));
      this.mealTable
            .withHeight("270px")
            .withWidth("400px");

      TextArea comment = group.buildAndBind("Komentaras", "comment", TextArea.class);
      comment.setNullRepresentation("");

      HorizontalLayout buttons = new MHorizontalLayout(new Button("Saugoti"), new Button("Uždaryti"))
            .withMargin(new MarginInfo(true, false, false, false));

      this.setContent(
            new HorizontalLayout(
                  new MVerticalLayout(
                        nameField,
                        birthDateField,
                        lastMealField,
                        mealTable,
                        new TableControlPanel(
                              click -> new UserMealSelectionPopup(mealRepository.loadAll(), messages),
                              click -> {
                                 Meal selected = mealTable.getValue();
                                 mealTable.getContainerDataSource().removeItem(selected);
                              }
                        ),
                        buttons
                  )
                        .withWidth("450px")
                        .withAlign(buttons, Alignment.BOTTOM_RIGHT),

                  new MVerticalLayout(photoWrapperLayout, cardCodeField, typeComboBox(), comment)
//                        .withAlign(photoWrapperLayout, Alignment.BOTTOM_CENTER)
            )
      );
      this.setModal(true);
   }

   private ComboBox typeComboBox() {
      ComboBox typeCombo = new EnumComboBox<>(PupilType.class);
      typeCombo.setRequired(true);
      group.bind(typeCombo, "type");
      typeCombo.setNullSelectionAllowed(false);
      return typeCombo;
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

   public MealData getModel() {
      return group.getItemDataSource().getBean();
   }

   public void edit(String cardCode) {
      UserMeal userMeal = pupilService.find(cardCode)
            .orElseThrow(() -> new IllegalStateException("Could not find user by cardCode"));

      this.birthDateField.setValue(
            StringUtils.isBlank(userMeal.getUser().getBirthDate())
                  ? messages.get(this, "noData")
                  : userMeal.getUser().getBirthDate()
      );

      this.lastMealField.setValue(
            mealService.lastMealEvent(cardCode)
                  .map(DATE_TIME_FORMAT::format)
                  .orElseGet(() -> messages.get(this, "noData"))
      );

      this.nameField.setValue(userMeal.getUser().getName());
      this.cardCodeField.setValue(userMeal.getUser().getCardCode());
      updateImage(userMeal.getUser().getBase16photo());
   }

   private void updateImage(String base16String) {
      Resource imageResource = imageService.imageResource(base16String);
      Image image = new Image(null, imageResource);
      image.setWidth("170px");
      image.setHeight("211px");
      photoWrapperLayout.removeAllComponents();
      photoWrapperLayout.addComponent(image);
   }
}
