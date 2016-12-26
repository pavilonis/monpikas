package lt.pavilonis.cmm.ui.user;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.Application;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.ui.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

@SpringComponent
@UIScope
public class MenuItemUsers extends MenuItem {

   @Autowired
   public MenuItemUsers(MessageSourceAdapter messages) {
      super(messages);
   }

   @Override
   public void collectLayoutElements(MVerticalLayout layout) {

      UserTable table = Application.context.getBean(UserTable.class);

      layout.add(Application.context.getBean(UserListFilterPanel.class), table)
            .expand(table);
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.USER;
   }
}
