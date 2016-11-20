package lt.pavilonis.cmm.ui;

import com.google.common.io.BaseEncoding;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.converter.StringToDateConverter;
import lt.pavilonis.cmm.representation.UserRepresentation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

@SpringComponent
@UIScope
public class UserEditPopup extends Window {

   static final String CAPTION_PHOTO = "User Photo";
   private static final Logger LOG = getLogger(UserEditPopup.class.getSimpleName());

   @Autowired
   private UserRestRepository userRepository;

   private final MTextField cardCode = new MTextField("Card Code");
   private final MTextField firstName = new MTextField("First name");
   private final MTextField lastName = new MTextField("Last name");
   private final MTextField group = new MTextField("Group");
   private final MTextField role = new MTextField("Role");
   private final MDateField birthDate = new MDateField("Birth Date");
   private final MVerticalLayout rightLayout = new MVerticalLayout()
         .withSpacing(true)
         .alignAll(Alignment.MIDDLE_LEFT);

   private Runnable postSaveUpdateAction = () -> {/**/};
   private UserRepresentation currentUser;
   private Image currentUserImage;
   private Consumer<UserRepresentation> saveAction;
   private MBeanFieldGroup<UserRepresentation> binding;

   public UserEditPopup() {

      setCaption("User settings");
      setResizable(false);
      setWidth("590px");
      setHeight("585px");

      ImageUploader uploadReceiver = new ImageUploader(this::updateUserPhoto);
      Upload imageUploader = new Upload(null, uploadReceiver);
      imageUploader.setImmediate(true);
      imageUploader.setButtonCaption("Select Image");
      imageUploader.addSucceededListener(uploadReceiver);

      Stream.of(firstName, lastName, birthDate, role, group, cardCode)
            .forEach(field -> field.setWidth("250px"));

      setContent(new MHorizontalLayout(
            new MVerticalLayout(
                  firstName,
                  lastName,
                  birthDate,
                  role,
                  group,
                  cardCode,
                  new MHorizontalLayout(
                        new MButton(FontAwesome.CHECK, "Save", (click) -> {
                           if (binding.isValid()) {
                              byte[] bytes = uploadReceiver.getScaledImageBytes();
                              if (bytes != null && bytes.length > 0) {
                                 currentUser.setBase16photo(BaseEncoding.base16().encode(bytes));
                              }
                              saveAction.accept(currentUser);
                              close();
                              Notification.show("Saved", Notification.Type.HUMANIZED_MESSAGE);
                           } else {
                              Notification.show("Incorrectly filled fields", Notification.Type.WARNING_MESSAGE);
                           }
                        }),
                        new MButton(FontAwesome.REMOVE, "Close", (click) -> close())
                  ).withSpacing(true).withMargin(new MarginInfo(true, false, false, false))
            ),
            rightLayout.with(imageUploader)
      ));

      birthDate.setConverter(new StringToDateConverter());
      birthDate.setDateFormat("yyyy-MM-dd");
      setModal(true);
   }

   public void edit(UserRepresentation user) {

      boolean persistent = user.getCardCode() != null;
      if (persistent) {
         // Edit existing
         currentUser = userRepository.load(user.getCardCode());
         saveAction = (userRepresentation) -> {
            userRepository.update(userRepresentation);
            postSaveUpdateAction.run();
         };
      } else {
         // Create new
         currentUser = user;
         saveAction = (userRepresentation) -> {
            userRepository.save(userRepresentation);
            postSaveUpdateAction.run();
         };
      }

      cardCode.setReadOnly(persistent);

      binding = BeanBinder.bind(currentUser, this);

      updateUserPhoto(imageResource(currentUser.getBase16photo()));

      VaadinUI.getCurrent().addWindow(this);
   }

   private Resource imageResource(String base16photo) {
      return StringUtils.isNoneBlank(base16photo) && BaseEncoding.base16().canDecode(base16photo)
            ? new StreamResource(() -> new ByteArrayInputStream(BaseEncoding.base16().decode(base16photo)), "img.png")
            : new ThemeResource("user_yellow_256.png");
   }

   public void addSaveOrUpdateListener(Runnable runnable) {
      postSaveUpdateAction = runnable;
   }

   private void updateUserPhoto(Resource imageResource) {
      if (currentUserImage != null)
         rightLayout.removeComponent(currentUserImage);

      Image image = new Image(CAPTION_PHOTO, imageResource);
      image.addStyleName("user-photo");
      rightLayout.addComponentAsFirst(currentUserImage = image);
   }
}
