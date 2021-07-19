package lt.pavilonis.cmm.school.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.Named;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.school.user.form.UserFormView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Hex;

import java.io.ByteArrayInputStream;
import java.util.List;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<User, Long, UserFilter> {

   private final UserListRepository userListRepository;
   private final UserRepository userRepository;
   private final PresenceTimeRepository presenceTimeRepository;
   private final String supervisorRole;

   public UserListController(UserListRepository userListRepository, UserRepository userRepository,
                             PresenceTimeRepository presenceTimeRepository,
                             @Value("${user.supervisor.organizationRole}") String supervisorRole) {
      this.userListRepository = userListRepository;
      this.userRepository = userRepository;
      this.presenceTimeRepository = presenceTimeRepository;
      this.supervisorRole = supervisorRole;
   }

   @Override
   protected ListGrid<User> createGrid() {
      return new UserGrid();
   }

   @Override
   protected AbstractFormController<User, Long> getFormController() {
      return new AbstractFormController<User, Long>(User.class) {

         @Override
         protected EntityRepository<User, Long, ?> getEntityRepository() {
            return userListRepository;
         }

         @Override
         protected FieldLayout<User> createFieldLayout(User model) {
            Resource image = StringUtils.isNotBlank(model.getBase16photo())
                  ? new StreamResource(() -> new ByteArrayInputStream(Hex.decode(model.getBase16photo())), "img.png")
                  : new ThemeResource("user_yellow_256.png");

            ComboBox<User> supervisorCombo = createSupervisorCombo(model.getOrganizationRole());
            return new UserFormView(presenceTimeRepository, model.getId(), image, supervisorCombo);
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
      };
   }

   @Override
   protected FilterPanel<UserFilter> createFilterPanel() {
      return new UserListFilterPanel(userRepository.loadRoles(), userRepository.loadGroups());
   }

   @Override
   protected EntityRepository<User, Long, UserFilter> getEntityRepository() {
      return userListRepository;
   }

   @Override
   protected Class<User> getEntityClass() {
      return User.class;
   }

   @Override
   public String getViewName() {
      return "users";
   }

   @Override
   public VaadinIcons getViewIcon() {
      return VaadinIcons.USER;
   }

   @Override
   public String getViewRole() {
      return "USERS";
   }

   @Override
   public String getViewGroupName() {
      return "school";
   }
}
