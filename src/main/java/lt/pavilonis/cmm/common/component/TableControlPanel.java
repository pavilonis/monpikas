package lt.pavilonis.cmm.common.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import lt.pavilonis.cmm.common.field.AButton;

import static com.vaadin.ui.Button.ClickListener;

public class TableControlPanel extends HorizontalLayout {


   public TableControlPanel(ClickListener listenerAdd, ClickListener listenerRemove) {
      this("add", "remove", listenerAdd, listenerRemove);
   }

   public TableControlPanel(
         String buttonCaptionPositive, String buttonCaptionNegative,
         ClickListener listenerPositive, ClickListener listenerNegative) {

      addComponents(
            new AButton(this.getClass().getSimpleName() + "." + buttonCaptionPositive)
                  .withIcon(VaadinIcons.PLUS)
                  .withClickListener(listenerPositive),

            new AButton(this.getClass().getSimpleName() + "." + buttonCaptionNegative)
                  .withIcon(VaadinIcons.WARNING)
                  .withClickListener(listenerNegative)
                  .withStyleName("redicon")
      );
   }
}