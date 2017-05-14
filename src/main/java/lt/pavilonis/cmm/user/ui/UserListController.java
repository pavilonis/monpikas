package lt.pavilonis.cmm.user.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.service.ImageService;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import lt.pavilonis.cmm.user.domain.PresenceTime;
import lt.pavilonis.cmm.user.domain.User;
import lt.pavilonis.cmm.user.form.UserFormView;
import lt.pavilonis.cmm.user.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<User, String, UserFilter> {

   @Autowired
   private UserRestRepository userRepository;

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
            return userRepository;
         }

         @Override
         protected FieldLayout<User> createFieldLayout() {
            List<PresenceTime> presenceTimeData = userRepository.loadPresenceTime(model.getCardCode());
            Resource image = imageService.imageResource(model.getBase16photo());
            return new UserFormView(presenceTimeData, image);
         }
      };
   }

   @Override
   protected FilterPanel<UserFilter> createFilterPanel() {
      return new UserListFilterPanel();
   }

   @Override
   protected EntityRepository<User, String, UserFilter> getEntityRepository() {
      return userRepository;
   }

   @Override
   protected Class<User> getEntityClass() {
      return User.class;
   }

   @Override
   protected Component getControlPanel() {
      return null;
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
