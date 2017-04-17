package lt.pavilonis.cmm;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;

public class MenuItem {
   private final String code;
   private final Class<? extends View> viewClass;
   private final VaadinIcons icon;

   public MenuItem(String code, Class<? extends View> viewClass, VaadinIcons icon) {
      this.code = code;
      this.viewClass = viewClass;
      this.icon = icon;
   }

   public String getCode() {
      return code;
   }

   public Class<? extends View> getViewClass() {
      return viewClass;
   }

   public VaadinIcons getIcon() {
      return icon;
   }
}
