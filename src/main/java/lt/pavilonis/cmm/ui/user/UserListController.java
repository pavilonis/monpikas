package lt.pavilonis.cmm.ui.user;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractViewController;

@SpringComponent
@UIScope
public class UserListController extends AbstractViewController {

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.USER;
   }

   @Override
   protected Class<? extends Component> getFilterPanelClass() {
      return UserListFilterPanel.class;
   }

   @Override
   protected Class<? extends Component> getMainLayoutClass() {
      return UserTable.class;
   }
}
