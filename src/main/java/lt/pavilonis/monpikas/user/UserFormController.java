package lt.pavilonis.monpikas.user;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.AbstractFormController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.FieldLayout;
import lt.pavilonis.monpikas.common.Named;
import lt.pavilonis.monpikas.user.form.UserFormView;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

public class UserFormController extends AbstractFormController<User, Long> {

   private final PresenceTimeRepository presenceTimeRepository;
   private final String supervisorRole;
   private final UserListRepository userListRepository;
   private final UserRepository userRepository;

   public UserFormController(UserListRepository userListRepository, PresenceTimeRepository presenceTimeRepository,
                             String supervisorRole, UserRepository userRepository) {
      super(User.class);
      this.presenceTimeRepository = presenceTimeRepository;
      this.supervisorRole = supervisorRole;
      this.userListRepository = userListRepository;
      this.userRepository = userRepository;
   }

   @Override
   protected EntityRepository<User, Long, ?> getEntityRepository() {
      return userListRepository;
   }

   @Override
   protected FieldLayout<User> createFieldLayout(User model) {
      Resource image = getUserImageResource(model.getBase64photo());
      ComboBox<User> supervisorCombo = createSupervisorCombo(model.getOrganizationRole());
      return new UserFormView(presenceTimeRepository, model.getId(), image, supervisorCombo);
   }

   private Resource getUserImageResource(String base64) {
      if (base64 == null) {
         return new ThemeResource("user_yellow_256.png");
      }
      return new StreamResource(() -> new ByteArrayInputStream(Base64.getDecoder().decode(base64)), "img.png");
   }

   private ComboBox<User> createSupervisorCombo(String organizationRole) {
      var result = new ComboBox<User>(App.translate(User.class, "supervisor"));
      if (supervisorRole.equalsIgnoreCase(organizationRole)) {
         result.setEnabled(false);
         return result;
      }

      List<User> supervisors = userRepository.load(new UserFilter(null, supervisorRole, null));
      result.setItems(supervisors);
      result.setItemCaptionGenerator(Named::getName);
      return result;
   }
}
