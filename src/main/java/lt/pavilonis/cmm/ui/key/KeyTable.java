package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.converter.LocalDateTimeConverter;
import lt.pavilonis.cmm.converter.ToStringConverterAdapter;
import lt.pavilonis.cmm.domain.KeyAction;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import lt.pavilonis.cmm.domain.ScannerRepresentation;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
class KeyTable extends ListTable<KeyRepresentation> {

   public KeyTable() {
      super(KeyRepresentation.class);
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("scanner", "keyNumber", "dateTime",
            "user.name", "user.group", "user.role", "keyAction");
   }

   @Override
   protected void customize(MessageSourceAdapter messageSource) {
      setConverter("dateTime", new LocalDateTimeConverter());
      setConverter("keyAction", new ToStringConverterAdapter<KeyAction>(KeyAction.class) {
         @Override
         protected String toPresentation(KeyAction model) {
            return messageSource.get(model, model.name());
         }
      });
      setConverter("scanner", new ToStringConverterAdapter<ScannerRepresentation>(ScannerRepresentation.class) {
         @Override
         protected String toPresentation(ScannerRepresentation model) {
            return messageSource.get(model, model.getName());
         }
      });
   }
}
