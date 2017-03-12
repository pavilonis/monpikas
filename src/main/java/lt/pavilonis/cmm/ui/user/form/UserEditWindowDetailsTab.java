package lt.pavilonis.cmm.ui.user.form;

import com.google.common.io.BaseEncoding;
import com.vaadin.data.Binder;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.field.ADateField;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.domain.UserRepresentation;

import java.util.function.Consumer;
import java.util.stream.Stream;

final class UserEditWindowDetailsTab extends HorizontalLayout {

   private final TextField cardCode = new ATextField(this.getClass(), "cardCode");
   private final TextField firstName = new ATextField(this.getClass(), "firstName");
   private final TextField lastName = new ATextField(this.getClass(), "lastName");
   private final TextField group = new ATextField(this.getClass(), "group");
   private final TextField role = new ATextField(this.getClass(), "role");
   private final ADateField birthDate = new ADateField(this.getClass(), "birthDate");
   //            .withConverter(new StringToDateConverter());
   private final VerticalLayout rightLayout = new VerticalLayout();

   private final UserRepresentation model;
   private Image currentUserImage;
   private final Binder<UserRepresentation> binder = new Binder<>(UserRepresentation.class);

   UserEditWindowDetailsTab(UserRepresentation model, Resource imageResource,
                            Consumer<UserRepresentation> saveAction, Button saveButton) {
      this.model = model;

      UserEditWindowDetailsTabImageUploader uploadReceiver =
            new UserEditWindowDetailsTabImageUploader(this::updateUserPhoto);
      Upload imageUploader = new Upload(null, uploadReceiver);
      imageUploader.setImmediateMode(true);
      imageUploader.setButtonCaption(App.translate(this, "selectImage"));
      imageUploader.addSucceededListener(uploadReceiver);

      Stream.of(firstName, lastName, birthDate, role, group, cardCode)
            .forEach(field -> field.setWidth("250px"));

      VerticalLayout fields = new VerticalLayout(firstName, lastName, birthDate, role, group);

      rightLayout.setSpacing(true);
      rightLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
      rightLayout.addComponents(cardCode, imageUploader);
      addComponents(fields, rightLayout);

      cardCode.setEnabled(false);
      cardCode.setReadOnly(true);

      updateUserPhoto(imageResource);

      binder.setBean(model);
      binder.bindInstanceFields(this);

      saveButton.addClickListener((click) -> {
         if (binder.isValid()) {
            byte[] bytes = uploadReceiver.getScaledImageBytes();
            if (bytes != null && bytes.length > 0) {
               this.model.setBase16photo(BaseEncoding.base16().encode(bytes));
            }
            saveAction.accept(this.model);
            Notification.show(App.translate(this, "saved"), Notification.Type.HUMANIZED_MESSAGE);
         } else {
            Notification.show(App.translate(this, "incorrectlyFilledFields"), Notification.Type.WARNING_MESSAGE);
         }
      });
      setHeight(430, Unit.PIXELS);
   }

   private void updateUserPhoto(Resource imageResource) {
      if (currentUserImage != null)
         rightLayout.removeComponent(currentUserImage);
      Image image = new Image(App.translate(this, "userPhoto"), imageResource);
      image.addStyleName("user-photo");
      rightLayout.addComponent(currentUserImage = image, 1);
   }
}
