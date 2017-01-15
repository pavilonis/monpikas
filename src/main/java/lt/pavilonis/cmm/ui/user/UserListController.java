package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class UserListController extends AbstractListController<UserRepresentation, String> {

   @Autowired
   private UserTable userTable;

   @Autowired
   private UserRestRepository userRepository;

   @Override
   protected MTable<UserRepresentation> getTable() {
      return userTable;
   }

   @Override
   protected EntityRepository<UserRepresentation, String> getEntityRepository() {
      return userRepository;
   }
}
