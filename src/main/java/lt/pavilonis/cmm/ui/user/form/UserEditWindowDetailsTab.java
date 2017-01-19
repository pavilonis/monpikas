package lt.pavilonis.cmm.ui.user.form;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.converter.StringToDateConverter;
import lt.pavilonis.cmm.domain.UserRepresentation;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.function.Consumer;
import java.util.stream.Stream;

final class UserEditWindowDetailsTab extends MHorizontalLayout {

   private final MTextField cardCode;
   private final MTextField firstName;
   private final MTextField lastName;
   private final MTextField group;
   private final MTextField role;
   private final MDateField birthDate;
   private final MVerticalLayout rightLayout = new MVerticalLayout()
         .withSpacing(true)
         .alignAll(Alignment.MIDDLE_LEFT);

   private final UserRepresentation model;
   private final MessageSourceAdapter messages;
   private Image currentUserImage;
   private final MBeanFieldGroup<UserRepresentation> binding;

    UserEditWindowDetailsTab(UserRepresentation model,
                                   Resource imageResource,
                                   Consumer<UserRepresentation> saveAction,
                                   Button saveButton,
                                   MessageSourceAdapter messages) {
      this.model = model;
      this.messages = messages;
      this.cardCode = new MTextField(messages.get(this, "cardCode"));
      this.firstName = new MTextField(messages.get(this, "firstName"));
      this.lastName = new MTextField(messages.get(this, "lastName"));
      this.group = new MTextField(messages.get(this, "group"));
      this.role = new MTextField(messages.get(this, "role"));
      this.birthDate = new MDateField(messages.get(this, "birthDate"));

      UserEditWindowDetailsTabImageUploader uploadReceiver =
            new UserEditWindowDetailsTabImageUploader(this::updateUserPhoto);
      Upload imageUploader = new Upload(null, uploadReceiver);
      imageUploader.setImmediate(true);
      imageUploader.setButtonCaption(messages.get(this, "selectImage"));
      imageUploader.addSucceededListener(uploadReceiver);

      Stream.of(firstName, lastName, birthDate, role, group, cardCode)
            .forEach(field -> field.setWidth("250px"));

      MVerticalLayout fields = new MVerticalLayout(firstName, lastName, birthDate, role, group, cardCode);
      add(fields, rightLayout.with(imageUploader));

      birthDate.setConverter(new StringToDateConverter());
      birthDate.setDateFormat("yyyy-MM-dd");

      cardCode.setEnabled(false);
      cardCode.setReadOnly(true);

      updateUserPhoto(imageResource);

      binding = BeanBinder.bind(model, this);

      saveButton.addClickListener((click) -> {
         if (binding.isValid()) {
            byte[] bytes = uploadReceiver.getScaledImageBytes();
            if (bytes != null && bytes.length > 0) {
               this.model.setBase16photo(BaseEncoding.base16().encode(bytes));
            }
            saveAction.accept(this.model);
            Notification.show(messages.get(this, "saved"), Notification.Type.HUMANIZED_MESSAGE);
         } else {
            Notification.show(messages.get(this, "incorrectlyFilledFields"), Notification.Type.WARNING_MESSAGE);
         }
      });
      setHeight("481px");
   }

   private void updateUserPhoto(Resource imageResource) {
      if (currentUserImage != null)
         rightLayout.removeComponent(currentUserImage);

      Image image = new Image(messages.get(this, "userPhoto"), imageResource);
      image.addStyleName("user-photo");
      rightLayout.addComponentAsFirst(currentUserImage = image);
   }
}
