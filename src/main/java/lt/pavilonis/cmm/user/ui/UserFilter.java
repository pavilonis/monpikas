package lt.pavilonis.cmm.user.ui;

public class UserFilter {

   private final String name;
   private final String role;
   private final String group;

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
