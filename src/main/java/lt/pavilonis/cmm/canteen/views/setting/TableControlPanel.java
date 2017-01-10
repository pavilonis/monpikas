package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.vaadin.ui.Button.ClickListener;

public class TableControlPanel extends MHorizontalLayout {

   public TableControlPanel(ClickListener listenerAdd, ClickListener listenerRemove) {
      addComponents(
            new MButton(FontAwesome.PLUS, "Pridėti", listenerAdd),
            new MButton(FontAwesome.WARNING, "Šalinti", listenerRemove).withStyleName("redicon")
      );
//      withAlign()
//      setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
//      setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
   }
}