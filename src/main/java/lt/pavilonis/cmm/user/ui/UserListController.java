package lt.pavilonis.cmm.user.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.user.domain.UserRepresentation;
import lt.pavilonis.cmm.user.form.UserFormController;
import lt.pavilonis.cmm.user.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<UserRepresentation, String, UserFilter> {

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   private UserListFilterPanel userListFilterPanel;

   @Autowired
   private UserFormController userFormController;

   @Override
   protected ListGrid<UserRepresentation> createGrid() {
      return new UserGrid();
   }

   @Override
   protected AbstractFormController<UserRepresentation, String> getFormController() {
      return userFormController;
   }

   @Override
   protected FilterPanel<UserFilter> createFilterPanel() {
      return userListFilterPanel;
   }

   @Override
   protected EntityRepository<UserRepresentation, String, UserFilter> getEntityRepository() {
      return userRepository;
   }

   @Override
   protected Class<UserRepresentation> getEntityClass() {
      return UserRepresentation.class;
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
