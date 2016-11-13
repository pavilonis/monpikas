package lt.pavilonis.cmm;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class UserTable extends MTable<UserRepresentation> {

   @Autowired
   public UserTable(UserRestRepository userRepository, UserEditPopup editPopup) {
      addBeans(userRepository.loadAll());
      withProperties("cardCode", "firstName", "lastName", "description", "pupil");
      withColumnHeaders("Card", "First name", "Last name", "Description", "Pupil");
      withGeneratedColumn("pupil", value -> value.isPupil() ? "✓" : null);
      withFullWidth();
      setSelectable(true);
      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            editPopup.edit(click.getRow());
         }
      });
   }
}
