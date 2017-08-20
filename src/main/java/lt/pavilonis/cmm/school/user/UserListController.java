package lt.pavilonis.cmm.school.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.api.rest.presence.PresenceTime;
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

import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<User, String, UserFilter> {

   @Autowired
   private UserListRepository userListRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private PresenceTimeRepository presenceTimeRepository;

   @Autowired
   private ImageService imageService;

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
            List<PresenceTime> presenceTimeData = presenceTimeRepository.load(model.getCardCode());
            Resource image = imageService.imageResource(model.getBase16photo());
            return new UserFormView(presenceTimeData, image);
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
   protected Optional<Component> getControlPanel() {
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
