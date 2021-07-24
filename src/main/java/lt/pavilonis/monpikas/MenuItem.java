package lt.pavilonis.monpikas;

import com.vaadin.icons.VaadinIcons;

public class MenuItem {

   private final String codeName;
   private final VaadinIcons icon;

   public MenuItem(String codeName, VaadinIcons icon) {
      this.codeName = codeName;
      this.icon = icon;
   }

   public String getCodeName() {
      return codeName;
   }

   public VaadinIcons getIcon() {
      return icon;
   }
}
