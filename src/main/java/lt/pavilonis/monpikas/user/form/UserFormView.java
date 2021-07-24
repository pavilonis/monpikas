package lt.pavilonis.monpikas.user.form;

import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.common.FieldLayout;
import lt.pavilonis.monpikas.common.field.ADateField;
import lt.pavilonis.monpikas.common.field.ATextField;
import lt.pavilonis.monpikas.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.monpikas.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.monpikas.user.PresenceTime;
import lt.pavilonis.monpikas.user.PresenceTimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class UserFormView extends FieldLayout<User> {

   private final TextField cardCode = new ATextField(this.getClass(), "cardCode");
   private final TextField name = new ATextField(this.getClass(), "name");
   private final TextField organizationGroup = new ATextField(this.getClass(), "organizationGroup");
   private final TextField organizationRole = new ATextField(this.getClass(), "organizationRole");
   private final DateField birthDate = new ADateField(this.getClass(), "birthDate");
   private final TextField base16photo = new TextField();
   private final ComboBox<User> supervisor;

   public UserFormView(PresenceTimeRepository presenceTimeRepository,
                       Long userId, Resource userImage, ComboBox<User> supervisorCombo) {
      this.supervisor = supervisorCombo;
      var presenceTimeGrid = new UserFormViewPresenceTimeGrid();
      var filterPanel = createFilterPanel(presenceTimeRepository, userId, presenceTimeGrid);
      var sheet = new TabSheet();

      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
      sheet.addTab(new VerticalLayout(filterPanel, presenceTimeGrid), App.translate(this, "hoursOfPresence"));
      sheet.addTab(new UserEditWindowDetailsTab(userImage, base16photo), App.translate(this, "editDetails"));

      addComponent(sheet);
   }

   private PeriodFilterPanel<IdPeriodFilter> createFilterPanel(PresenceTimeRepository presenceTimeRepository,
                                                               Long userId,
                                                               UserFormViewPresenceTimeGrid presenceTimeGrid) {
      var filterPanel = new PeriodFilterPanel<>();
      DateField periodStart = filterPanel.getPeriodStart();
      DateField periodEnd = filterPanel.getPeriodEnd();

      periodStart.setValue(LocalDate.now().minusMonths(1));

      Runnable updateGrid = () -> {
         List<PresenceTime> items = presenceTimeRepository.load(userId, periodStart.getValue(), periodEnd.getValue());
         presenceTimeGrid.setItems(items);
      };

      filterPanel.addResetClickListener(click -> filterPanel.fieldReset());
      filterPanel.addSearchClickListener(event -> updateGrid.run());
      updateGrid.run();
      return filterPanel;
   }

   private final class UserEditWindowDetailsTab extends HorizontalLayout {

      private Image currentUserImage;

      private UserEditWindowDetailsTab(Resource imageResource, TextField base16ImageTextField) {
         setMargin(true);

         Stream.<Component>of(name, birthDate, organizationRole, organizationGroup, cardCode, supervisor)
               .forEach(field -> field.setWidth("250px"));

         var layoutLeft = new VerticalLayout(name, birthDate, organizationRole, organizationGroup, supervisor);
         layoutLeft.setMargin(false);

         var layoutRight = new VerticalLayout();
         layoutRight.setMargin(false);
         layoutRight.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
         layoutRight.addComponents(cardCode, createUploader(base16ImageTextField, layoutRight));

         addComponents(layoutLeft, layoutRight);
         updateUserPhoto(imageResource, layoutRight);
         setHeight(460, Unit.PIXELS);
      }

      private Upload createUploader(TextField imageTextField, VerticalLayout layout) {
         var uploadReceiver = new UserFormViewImageUploader((newImage, base16ImageString) -> {
            updateUserPhoto(newImage, layout);
            imageTextField.setValue(base16ImageString);
         });
         Upload imageUploader = new Upload(null, uploadReceiver);
         imageUploader.setImmediateMode(true);
         imageUploader.setButtonCaption(App.translate(UserFormView.class, "selectImage"));
         imageUploader.addSucceededListener(uploadReceiver);
         return imageUploader;
      }

      private void updateUserPhoto(Resource imageResource, VerticalLayout layout) {
         if (currentUserImage != null) {
            layout.removeComponent(currentUserImage);
         }
         var image = new Image(App.translate(UserFormView.class, "userPhoto"), imageResource);
         image.addStyleName("user-photo");
         layout.addComponent(currentUserImage = image, 1);
      }
   }

}
