package lt.pavilonis.cmm.school.user.form;

import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.api.rest.presence.PresenceTime;
import lt.pavilonis.cmm.api.rest.presence.PresenceTimeRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.IdPeriodFilter;
import lt.pavilonis.cmm.common.ui.filter.PeriodFilterPanel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class UserFormView extends FieldLayout<User> {

   private final TextField cardCode = new ATextField(this.getClass(), "cardCode");
   private final TextField firstName = new ATextField(this.getClass(), "firstName");
   private final TextField lastName = new ATextField(this.getClass(), "lastName");
   private final TextField group = new ATextField(this.getClass(), "group");
   private final TextField role = new ATextField(this.getClass(), "role");
   private final TextField birthDate = new ATextField(this.getClass(), "birthDate");
   private final TextField base16photo = new TextField();

   public UserFormView(PresenceTimeRepository presenceTimeRepository, String cardCode, Resource userImage) {
      TabSheet sheet = new TabSheet();
      sheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
      sheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

      UserFormViewPresenceTimeGrid presenceTimeGrid = new UserFormViewPresenceTimeGrid();

      sheet.addTab(
            new VerticalLayout(
                  createFilterPanel(presenceTimeRepository, cardCode, presenceTimeGrid),
                  presenceTimeGrid
            ),
            App.translate(this, "hoursOfPresence")
      );

      sheet.addTab(
            new UserEditWindowDetailsTab(userImage, base16photo),
            App.translate(this, "editDetails")
      );

      addComponent(sheet);
   }

   protected PeriodFilterPanel<IdPeriodFilter> createFilterPanel(PresenceTimeRepository presenceTimeRepository,
                                                                 String cardCode,
                                                                 UserFormViewPresenceTimeGrid presenceTimeGrid) {

      PeriodFilterPanel<IdPeriodFilter> filterPanel = new PeriodFilterPanel<>();

      DateField periodStart = filterPanel.getPeriodStart();
      DateField periodEnd = filterPanel.getPeriodEnd();
      periodStart.setValue(LocalDate.now().minusMonths(1));

      Runnable updateGrid = () -> {
         List<PresenceTime> items = presenceTimeRepository.load(
               cardCode,
               periodStart.getValue(),
               periodEnd.getValue()
         );
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
         UserFormViewImageUploader uploadReceiver = new UserFormViewImageUploader((newImage, base16ImageString) -> {
            updateUserPhoto(newImage);
            base16ImageTextField.setValue(base16ImageString);
         });
         Upload imageUploader = new Upload(null, uploadReceiver);
         imageUploader.setImmediateMode(true);
         imageUploader.setButtonCaption(App.translate(UserFormView.class, "selectImage"));
         imageUploader.addSucceededListener(uploadReceiver);

         Stream.of(firstName, lastName, birthDate, role, group, cardCode)
               .forEach(field -> field.setWidth("250px"));

         VerticalLayout fields = new VerticalLayout(firstName, lastName, birthDate, role, group);
         fields.setMargin(false);

         rightLayout.setMargin(false);
         rightLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
         rightLayout.addComponents(cardCode, imageUploader);
         addComponents(fields, rightLayout);

         cardCode.setEnabled(false);
         cardCode.setReadOnly(true);

         updateUserPhoto(imageResource);

         setHeight(460, Unit.PIXELS);
      }

      private void updateUserPhoto(Resource imageResource) {
         if (currentUserImage != null)
            rightLayout.removeComponent(currentUserImage);
         Image image = new Image(App.translate(UserFormView.class, "userPhoto"), imageResource);
         image.addStyleName("user-photo");
         rightLayout.addComponent(currentUserImage = image, 1);
      }
   }

}
