package lt.pavilonis.monpikas.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import lt.pavilonis.monpikas.App;
import lt.pavilonis.monpikas.common.AbstractFormController;
import lt.pavilonis.monpikas.common.AbstractListController;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.FieldLayout;
import lt.pavilonis.monpikas.common.ListGrid;
import lt.pavilonis.monpikas.common.Named;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import lt.pavilonis.monpikas.user.form.UserFormView;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.util.Base64;
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
      return new UserFormController(userListRepository, presenceTimeRepository, supervisorRole, userRepository);
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
