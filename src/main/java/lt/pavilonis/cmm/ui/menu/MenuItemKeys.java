package lt.pavilonis.cmm.ui.menu;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.Application;
import lt.pavilonis.cmm.ui.key.KeyListFilterPanel;
import lt.pavilonis.cmm.ui.key.KeyTable;
import org.vaadin.viritin.layouts.MVerticalLayout;

@SpringComponent
@UIScope
public class MenuItemKeys extends MenuItem {

   public MenuItemKeys() {
      setCaption("Keys");
      setIcon(FontAwesome.KEY);
   }

   private KeyListFilterPanel filterPanel;
   private KeyTable keyTable;

   @Override
   public void collectLayoutElements(MVerticalLayout layout) {
      layout.add(filterPanel(), keyTable())
            .expand(keyTable);
   }

   private Component filterPanel() {
      return filterPanel == null
            ? filterPanel = Application.context.getBean(KeyListFilterPanel.class)
            : filterPanel;
   }

   private Component keyTable() {
      return keyTable == null
            ? keyTable = Application.context.getBean(KeyTable.class)
            : keyTable;
   }
}
