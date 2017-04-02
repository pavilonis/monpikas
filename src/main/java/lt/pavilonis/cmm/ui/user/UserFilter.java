package lt.pavilonis.cmm.ui.user;

public class UserFilter {
   private final String name;
   private final String role;
   private final String group;

   public UserFilter() {
      this(null, null, null);
   }

   public UserFilter(String name, String role, String group) {
      this.name = name;
      this.role = role;
      this.group = group;
   }

   public String getName() {
      return name;
   }

   public String getRole() {
      return role;
   }

   public String getGroup() {
      return group;
   }
}
