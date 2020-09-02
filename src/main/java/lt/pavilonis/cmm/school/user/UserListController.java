package lt.pavilonis.cmm.school.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.api.rest.presence.PresenceTimeRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.service.ImageService;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.school.user.form.UserFormView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<User, String, UserFilter> {

   private final UserListRepository userListRepository;
   private final UserRepository userRepository;
   private final PresenceTimeRepository presenceTimeRepository;
   private final ImageService imageService;

   public UserListController(UserListRepository userListRepository, UserRepository userRepository,
                             PresenceTimeRepository presenceTimeRepository, ImageService imageService) {
      this.userListRepository = userListRepository;
      this.userRepository = userRepository;
      this.presenceTimeRepository = presenceTimeRepository;
      this.imageService = imageService;
   }

   @Override
   protected ListGrid<User> createGrid() {
      return new UserGrid();
   }

   @Override
   protected AbstractFormController<User, String> getFormController() {
      return new AbstractFormController<User, String>(User.class) {

         @Override
         protected EntityRepository<User, String, ?> getEntityRepository() {
            return userListRepository;
         }

         @Override
         protected FieldLayout<User> createFieldLayout(User model) {
            Resource image = imageService.imageResource(model.getBase16photo());
            return new UserFormView(presenceTimeRepository, model.getCardCode(), image);
         }
      };
   }

   @Override
   protected FilterPanel<UserFilter> createFilterPanel() {
      return new UserListFilterPanel(
            userRepository.loadRoles(),
            userRepository.loadGroups()
      );
   }

   @Override
   protected EntityRepository<User, String, UserFilter> getEntityRepository() {
      return userListRepository;
   }

   @Override
   protected Class<User> getEntityClass() {
      return User.class;
   }

   @Override
   protected Optional<Component> getControlPanel(Component mainArea) {
      return Optional.empty();
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
