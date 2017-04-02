package lt.pavilonis.cmm.ui.key;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.KeyAction;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.domain.ScannerRepresentation;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringComponent
@UIScope
class KeyListGrid extends ListGrid<KeyRepresentation> {

   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm");

   public KeyListGrid() {
      super(KeyRepresentation.class);
   }

   @Override
   protected List<String> getProperties(Class<KeyRepresentation> type) {
      return Arrays.asList("scanner", "keyNumber", "dateTime",
            "user.name", "user.group", "user.role", "keyAction");
   }

   @Override
   protected Map<String, ValueProvider<KeyRepresentation, ?>> getCustomColumns() {
      return ImmutableMap.<String, ValueProvider<KeyRepresentation, ?>>builder()
            .put("user.name", key -> key.getUser().getName())
            .put("user.role", key -> key.getUser().getRole())
            .put("user.group", key -> key.getUser().getGroup())
            .put("dateTime", key -> DATE_TIME_FORMAT.format(key.getDateTime()))
            .put("keyAction", key -> messages.get(KeyAction.class, key.getKeyAction().name()))
            .put("scanner", key -> messages.get(ScannerRepresentation.class, key.getScanner().getName()))
            .build();
   }
}
