package lt.pavilonis.cmm.ui.key;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.repository.KeyRestRepository;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.domain.KeyRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class KeyTable extends MTable<KeyRepresentation> {

   @Autowired
   public KeyTable(KeyRestRepository keys, MessageSourceAdapter messages) {

      addBeans(keys.loadAll());
      withProperties("keyNumber", "dateTime", "user");
      withColumnHeaders(
            messages.get(this, "keyNumber"),
            messages.get(this, "dateTime"),
            messages.get(this, "user")
      );
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
//      setSortContainerPropertyId("dateTime");
      setCacheRate(3);
      withFullWidth();
      setSelectable(true);
      setNullSelectionAllowed(false);
   }
}
