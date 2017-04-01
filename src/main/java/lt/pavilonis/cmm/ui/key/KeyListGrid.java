package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.KeyRepresentation;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
class KeyListGrid extends ListGrid<KeyRepresentation> {

   public KeyListGrid() {
      super(KeyRepresentation.class);
   }

   @Override
   protected List<String> getProperties(Class<KeyRepresentation> type) {
      return Arrays.asList("scanner", "keyNumber", "dateTime",
            "user.name", "user.group", "user.role", "keyAction");
   }

   @Override
   protected void addCustomColumns() {
      addColumn(key -> key.getUser().getName())
            .setId("user.name");
      addColumn(key -> key.getUser().getRole())
            .setId("user.role");
      addColumn(key -> key.getUser().getGroup())
            .setId("user.group");
   }

   @Override
   protected void customize() {
//      setConverter("dateTime", new LocalDateTimeConverter());
//      setConverter("keyAction", new ToStringConverterAdapter<KeyAction>(KeyAction.class) {
//         @Override
//         protected String toPresentation(KeyAction model) {
//            return messageSource.get(model, model.name());
//         }
//      });
//      setConverter("scanner", new ToStringConverterAdapter<ScannerRepresentation>(ScannerRepresentation.class) {
//         @Override
//         protected String toPresentation(ScannerRepresentation model) {
//            return messageSource.get(model, model.getName());
//         }
//      });
   }
}
