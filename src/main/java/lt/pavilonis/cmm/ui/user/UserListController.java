package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<UserRepresentation, String, UserFilter> {

   @Autowired
   private UserGrid userTable;

   @Autowired
   private UserRestRepository userRepository;

   @Autowired
   private UserListFilterPanel userListFilterPanel;

   @Override
   protected ListGrid<UserRepresentation> createGrid() {
      return userTable;
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
}
