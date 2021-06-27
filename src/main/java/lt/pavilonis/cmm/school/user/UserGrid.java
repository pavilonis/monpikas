package lt.pavilonis.cmm.school.user;

import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.common.ListGrid;

import java.util.List;

final class UserGrid extends ListGrid<User> {

   public UserGrid() {
      super(User.class);
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("cardCode", "name", "organizationRole", "organizationGroup");
   }

   @Override
   protected void customize() {
      sort("name");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return List.of("cardCode");
   }
}
