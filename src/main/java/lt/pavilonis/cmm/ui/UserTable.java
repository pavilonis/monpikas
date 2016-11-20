package lt.pavilonis.cmm.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.UserRestRepository;
import lt.pavilonis.cmm.representation.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
public class UserTable extends MTable<UserRepresentation> {

   @Autowired
   public UserTable(UserRestRepository userRepository, UserEditPopup editPopup) {
      addBeans(userRepository.loadAll());
      withProperties("cardCode", "firstName", "lastName", "role", "group");
      withColumnHeaders("Card", "First Name", "Last Name", "Role", "Group");
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setCacheRate(3);
      setColumnCollapsed("cardCode", true);
      withFullWidth();
      setSelectable(true);
      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            editPopup.edit(click.getRow());
         }
      });
      editPopup.addSaveOrUpdateListener(() -> setBeans(userRepository.loadAll()));
   }
}
