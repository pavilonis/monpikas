package lt.pavilonis.monpikas.user;

import com.vaadin.data.provider.Query;

final class UserFilter {

   private final String name;
   private final String role;
   private final String group;
   private Query<User, UserFilter> query;

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
      return query == null ? null : query.getOffset();
   }

   public Integer getLimit() {
      return query == null ? null : query.getLimit();
   }

   public UserFilter withQuery(Query<User, UserFilter> query) {
      this.query = query;
      return this;
   }

   public Query<User, UserFilter> getQuery() {
      return query;
   }
}
