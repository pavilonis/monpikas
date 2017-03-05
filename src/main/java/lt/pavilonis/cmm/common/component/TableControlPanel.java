package lt.pavilonis.cmm.common.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import lt.pavilonis.cmm.App;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.vaadin.ui.Button.ClickListener;

public class TableControlPanel extends MHorizontalLayout {


   public TableControlPanel(ClickListener listenerAdd, ClickListener listenerRemove) {
      this("add", "remove", listenerAdd, listenerRemove);
   }

   public TableControlPanel(
         String buttonCaptionPositive,
         String buttonCaptionNegative,
         ClickListener listenerPositive,
         ClickListener listenerNegative) {

      add(
            new MButton(
                  VaadinIcons.PLUS,
                  App.translate(this, buttonCaptionPositive),
                  listenerPositive
            ),

            new MButton(
                  VaadinIcons.WARNING,
                  App.translate(this, buttonCaptionNegative),
                  listenerNegative
            ).withStyleName("redicon")
      );
   }
}