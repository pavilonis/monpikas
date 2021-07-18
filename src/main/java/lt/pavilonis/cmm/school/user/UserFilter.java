package lt.pavilonis.cmm.school.user;

public class UserFilter {

   private final String name;
   private final String role;
   private final String group;
   private Integer offset;
   private Integer limit;

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

   public Integer getOffset() {
      return offset;
   }

   public Integer getLimit() {
      return limit;
   }

   public UserFilter withOffset(int offset) {
      this.offset = offset;
      return this;
   }

   public UserFilter withLimit(int limit) {
      this.limit = limit;
      return this;
   }
}
