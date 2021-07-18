package lt.pavilonis.cmm.school.user.form;

import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;
import lt.pavilonis.cmm.school.user.PresenceTime;
import lt.pavilonis.cmm.school.user.PresenceTimeRepository;

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

      private final VerticalLayout rightLayout = new VerticalLayout();
      private Image currentUserImage;

      private UserEditWindowDetailsTab(Resource imageResource, TextField base16ImageTextField) {
         setMargin(true);

         Stream.of(name, birthDate, organizationRole, organizationGroup, cardCode)
               .forEach(field -> field.setWidth("250px"));

         var fields = new VerticalLayout(name, birthDate, organizationRole, organizationGroup, supervisor);
         fields.setMargin(false);

         rightLayout.setMargin(false);
         rightLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
         rightLayout.addComponents(cardCode, createUploader(base16ImageTextField));

         addComponents(fields, rightLayout);
         updateUserPhoto(imageResource);
         setHeight(460, Unit.PIXELS);
      }

      private Upload createUploader(TextField imageTextField) {
         var uploadReceiver = new UserFormViewImageUploader((newImage, base16ImageString) -> {
            updateUserPhoto(newImage);
            imageTextField.setValue(base16ImageString);
         });
         Upload imageUploader = new Upload(null, uploadReceiver);
         imageUploader.setImmediateMode(true);
         imageUploader.setButtonCaption(App.translate(UserFormView.class, "selectImage"));
         imageUploader.addSucceededListener(uploadReceiver);
         return imageUploader;
      }

      private void updateUserPhoto(Resource imageResource) {
         if (currentUserImage != null) {
            rightLayout.removeComponent(currentUserImage);
         }
         var image = new Image(App.translate(UserFormView.class, "userPhoto"), imageResource);
         image.addStyleName("user-photo");
         rightLayout.addComponent(currentUserImage = image, 1);
      }
   }

}
