package lt.pavilonis.monpikas.user;

import com.vaadin.data.ValueProvider;
import lt.pavilonis.monpikas.common.ListGrid;

import java.util.List;
import java.util.Map;

final class UserGrid extends ListGrid<User> {

   public UserGrid() {
      super(User.class);
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("id", "cardCode", "name", "organizationRole", "organizationGroup", "supervisor");
   }

   @Override
   protected Map<String, ValueProvider<User, ?>> getCustomColumns() {
      return Map.of("supervisor", user -> user.getSupervisor() == null
            ? null
            : user.getSupervisor().getName());
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
