package lt.pavilonis.cmm.school.key.ui;

import com.vaadin.data.ValueProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.key.KeyAction;
import lt.pavilonis.cmm.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SpringComponent
@UIScope
class KeyListGrid extends ListGrid<Key> {

   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm");

   public KeyListGrid() {
      super(Key.class);
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("scanner", "keyNumber", "dateTime", "user.id", "user.name", "user.role", "user.group", "keyAction", "supervisor");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return List.of("supervisor", "user.id");
   }

   @Override
   protected Map<String, ValueProvider<Key, ?>> getCustomColumns() {
      return Map.of(
            "user.id", key -> key.getUser().getId(),
            "user.name", key -> key.getUser().getName(),
            "user.role", key -> key.getUser().getOrganizationRole(),
            "user.group", key -> key.getUser().getOrganizationGroup(),
            "dateTime", key -> DATE_TIME_FORMAT.format(key.getDateTime()),
            "keyAction", key -> messages.get(KeyAction.class, key.getKeyAction().name()),
            "scanner", key -> key.getScanner().getName(),
            "supervisor", key -> key.getUser().getSupervisor() == null ? null : key.getUser().getSupervisor().getName()
      );
   }
}
