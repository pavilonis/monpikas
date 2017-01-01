package lt.pavilonis.cmm.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

public interface ListController {

   Button getMenuButton();

   // Added for sorting
   String getMenuButtonCaption();

   Layout getListLayout();
}
