package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.converter.LocalDateTimeConverter;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class KeyTable extends MTable<KeyRepresentation> {

   @Autowired
   public KeyTable(MessageSourceAdapter messages) {

      withProperties("keyNumber", "dateTime", "user.firstName", "user.lastName", "user.group", "user.role");
      withColumnHeaders(
            messages.get(this, "keyNumber"),
            messages.get(this, "dateTime"),
            messages.get(this, "user.firstName"),
            messages.get(this, "user.lastName"),
            messages.get(this, "user.group"),
            messages.get(this, "user.role")
      );
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setCacheRate(3);
      withFullWidth();
      setSelectable(true);
      setNullSelectionAllowed(false);
      setConverter("dateTime", new LocalDateTimeConverter());
   }
}
