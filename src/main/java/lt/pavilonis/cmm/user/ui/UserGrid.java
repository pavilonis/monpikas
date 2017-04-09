package lt.pavilonis.cmm.user.ui;

import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.user.domain.UserRepresentation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class UserGrid extends ListGrid<UserRepresentation> {

   public UserGrid() {
      super(UserRepresentation.class);
   }

   @Override
   protected List<String> getProperties(Class<UserRepresentation> type) {
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
