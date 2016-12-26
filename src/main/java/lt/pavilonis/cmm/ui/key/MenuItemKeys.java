package lt.pavilonis.cmm.ui.key;

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
public class MenuItemKeys extends MenuItem {

   @Autowired
   public MenuItemKeys(MessageSourceAdapter messages) {
      super(messages);
   }

   @Override
   public void collectLayoutElements(MVerticalLayout layout) {
      KeyTable table = Application.context.getBean(KeyTable.class);
      layout.add(Application.context.getBean(KeyListFilterPanel.class), table)
            .expand(table);
   }

   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.KEY;
   }
}
