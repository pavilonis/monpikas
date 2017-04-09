package lt.pavilonis.cmm.common.field;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.service.MessageSourceAdapter;

public class AButton extends Button {

   public AButton(String labelCode) {
      String caption = App.context.getBean(MessageSourceAdapter.class)
            .get(labelCode);

      this.setCaption(caption);
   }

   public AButton(Object objectOrClass, String property) {
      String caption = App.translate(objectOrClass, property);
      this.setCaption(caption);
   }

   public AButton withClickShortcut(int shortcut) {
      this.setClickShortcut(shortcut);
      return this;
   }

   public AButton withClickListener(ClickListener clickListener) {
      this.addClickListener(clickListener);
      return this;
   }

   public AButton withIcon(VaadinIcons icon) {
      this.setIcon(icon);
      return this;
   }

   public AButton withStyleName(String styleName) {
      this.addStyleName(styleName);
      return this;
   }
}
