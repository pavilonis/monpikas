package lt.pavilonis.cmm.ui.key;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.common.AbstractController;

@SpringComponent
@UIScope
public class KeyListController extends AbstractController {
   @Override
   protected FontAwesome getMenuIcon() {
      return FontAwesome.KEY;
   }

   @Override
   protected Class<? extends Component> getFilterPanelClass() {
      return KeyListFilterPanel.class;
   }

   @Override
   protected Class<? extends Component> getTableClass() {
      return KeyTable.class;
   }
}
