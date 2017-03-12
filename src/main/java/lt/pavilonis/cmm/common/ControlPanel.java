package lt.pavilonis.cmm.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import lt.pavilonis.cmm.common.field.AButton;

public class ControlPanel extends HorizontalLayout {

   public ControlPanel(Button.ClickListener actionCreate, Button.ClickListener actionDelete) {

      Button buttonCreate = new AButton(this, "buttonCreate")
            .withIcon(VaadinIcons.PLUS)
            .withClickListener(actionCreate);

      Button buttonDelete = new AButton(this, "buttonDelete")
            .withIcon(VaadinIcons.WARNING)
            .withClickListener(actionDelete)
            .withStyleName("redicon");

      this.addComponents(buttonCreate, buttonDelete);
      this.setMargin(false);
   }
}
