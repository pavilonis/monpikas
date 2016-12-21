package lt.pavilonis.cmm.ui.menu;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.vaadin.viritin.layouts.MVerticalLayout;

@SpringComponent
@UIScope
public class MenuItemKeyLog extends MenuItem {
   public MenuItemKeyLog() {
      setCaption("Key Log");
      setIcon(FontAwesome.BOOK);
   }

   @Override
   public void collectLayoutElements(MVerticalLayout layout) {
   }
}
