package lt.pavilonis.monpikas.common.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import lt.pavilonis.monpikas.common.field.AButton;

import static com.vaadin.ui.Button.ClickListener;

public class GridControlPanel extends HorizontalLayout {


   public GridControlPanel(ClickListener listenerAdd, ClickListener listenerRemove) {
      this("add", "remove", listenerAdd, listenerRemove);
   }

   public GridControlPanel(
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