package lt.pavilonis.cmm.ui;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload;
import lt.pavilonis.cmm.converter.StringToDateConverter;
import lt.pavilonis.cmm.representation.UserRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class UserDetailsForm extends MHorizontalLayout {

   static final String CAPTION_PHOTO = "User Photo";

   private final MTextField cardCode = new MTextField("Card Code");
   private final MTextField firstName = new MTextField("First name");
   private final MTextField lastName = new MTextField("Last name");
   private final MTextField group = new MTextField("Group");
   private final MTextField role = new MTextField("Role");
   private final MDateField birthDate = new MDateField("Birth Date");
   private final MVerticalLayout rightLayout = new MVerticalLayout()
         .withSpacing(true)
         .alignAll(Alignment.MIDDLE_LEFT);

   private final UserRepresentation model;
   private Image currentUserImage;
   private final Consumer<UserRepresentation> saveAction;
   private final MBeanFieldGroup<UserRepresentation> binding;

   public UserDetailsForm(UserRepresentation model, Consumer<UserRepresentation> saveAction) {
      this.model = model;
      this.saveAction = saveAction;

      ImageUploader uploadReceiver = new ImageUploader(this::updateUserPhoto);
      Upload imageUploader = new Upload(null, uploadReceiver);
      imageUploader.setImmediate(true);
      imageUploader.setButtonCaption("Select Image");
      imageUploader.addSucceededListener(uploadReceiver);

      Stream.of(firstName, lastName, birthDate, role, group, cardCode)
            .forEach(field -> field.setWidth("250px"));


      add(
            new MVerticalLayout(firstName, lastName, birthDate, role, group, cardCode),
            rightLayout.with(imageUploader)
      );

      birthDate.setConverter(new StringToDateConverter());
      birthDate.setDateFormat("yyyy-MM-dd");

      cardCode.setEnabled(false);
      cardCode.setReadOnly(true);

      updateUserPhoto(imageResource(model.getBase16photo()));

      binding = BeanBinder.bind(model, this);
   }

   private Resource imageResource(String base16photo) {
      return StringUtils.isNoneBlank(base16photo) && BaseEncoding.base16().canDecode(base16photo)
            ? new StreamResource(() -> new ByteArrayInputStream(BaseEncoding.base16().decode(base16photo)), "img.png")
            : new ThemeResource("user_yellow_256.png");
   }

   private void updateUserPhoto(Resource imageResource) {
      if (currentUserImage != null)
         rightLayout.removeComponent(currentUserImage);

      Image image = new Image(CAPTION_PHOTO, imageResource);
      image.addStyleName("user-photo");
      rightLayout.addComponentAsFirst(currentUserImage = image);
   }
}
