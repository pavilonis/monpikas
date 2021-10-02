package lt.pavilonis.monpikas;

import com.vaadin.icons.VaadinIcons;

final class MenuItem {

   private final String codeName;
   private final VaadinIcons icon;

   MenuItem(String codeName, VaadinIcons icon) {
      this.codeName = codeName;
      this.icon = icon;
   }

   String getCodeName() {
      return codeName;
   }

   VaadinIcons getIcon() {
      return icon;
   }
}
