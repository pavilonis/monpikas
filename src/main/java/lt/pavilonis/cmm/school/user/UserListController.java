package lt.pavilonis.cmm.school.user;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.api.rest.presence.PresenceTimeRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.school.user.form.UserFormView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;

import java.io.ByteArrayInputStream;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<User, Long, UserFilter> {

   private final UserListRepository userListRepository;
   private final UserRepository userRepository;
   private final PresenceTimeRepository presenceTimeRepository;

   public UserListController(UserListRepository userListRepository, UserRepository userRepository,
                             PresenceTimeRepository presenceTimeRepository) {
      this.userListRepository = userListRepository;
      this.userRepository = userRepository;
      this.presenceTimeRepository = presenceTimeRepository;
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

            return new UserFormView(presenceTimeRepository, model.getCardCode(), image);
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
