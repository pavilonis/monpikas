package lt.pavilonis.cmm;

import java.util.List;

public class MenuLinkGroup {
   private final String codeName;
   private final String requiredRole;
   private final List<MenuLink> links;

   public MenuLinkGroup(String codeName, String requiredRole, List<MenuLink> links) {
      this.codeName = codeName;
      this.requiredRole = requiredRole;
      this.links = links;
   }

   public String getCodeName() {
      return codeName;
   }

   public String getRequiredRole() {
      return requiredRole;
   }

   public List<MenuLink> getLinks() {
      return links;
   }
}
