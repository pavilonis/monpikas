package lt.pavilonis.cmm.user.ui;

import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.user.domain.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class UserGrid extends ListGrid<User> {

   public UserGrid() {
      super(User.class);
   }

   @Override
   protected List<String> columnOrder() {
      return Arrays.asList("cardCode", "firstName", "lastName", "role", "group");
   }

   @Override
   protected void customize() {
      sort("lastName");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("cardCode");
   }
}
