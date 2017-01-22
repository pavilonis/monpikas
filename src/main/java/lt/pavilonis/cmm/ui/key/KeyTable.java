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
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
class KeyTable extends ListTable<KeyRepresentation> {

   @Autowired //TODO move message somewhere super
   public KeyTable(MessageSourceAdapter messages) {

      withProperties("scanner", "keyNumber", "dateTime", "user.name",
            "user.group", "user.role", "keyAction");

      setColumnHeaders(
            messages.get(this, "scanner"),
            messages.get(this, "keyNumber"),
            messages.get(this, "dateTime"),
            messages.get(this, "user.name"),
            messages.get(this, "user.group"),
            messages.get(this, "user.role"),
            messages.get(this, "keyAction")
      );

      setConverter("dateTime", new LocalDateTimeConverter());
      setConverter("keyAction", new ToStringConverterAdapter<KeyAction>(KeyAction.class) {
         @Override
         protected String toPresentation(KeyAction model) {
            return messages.get(model, model.name());
         }
      });
      setConverter("scanner", new ToStringConverterAdapter<ScannerRepresentation>(ScannerRepresentation.class) {
         @Override
         protected String toPresentation(ScannerRepresentation model) {
            return messages.get(model, model.getName());
         }
      });
   }
}
