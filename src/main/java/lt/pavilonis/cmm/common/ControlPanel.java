package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

public class ControlPanel extends MHorizontalLayout {

   public ControlPanel(MessageSourceAdapter messages,
                       Button.ClickListener actionCreate,
                       Button.ClickListener actionDelete) {

      MButton buttonCreate = new MButton(FontAwesome.PLUS, messages.get(this, "buttonCreate"), actionCreate);
      MButton buttonDelete = new MButton(FontAwesome.WARNING, messages.get(this, "buttonDelete"), actionDelete)
            .withStyleName("redicon");

      this.add(buttonCreate, buttonDelete);

      this.setMargin(false);
   }
}
