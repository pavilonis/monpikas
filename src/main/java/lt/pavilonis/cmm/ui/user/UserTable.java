package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.ui.user.form.UserEditWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

@SpringComponent
@UIScope
class UserTable extends MTable<UserRepresentation> {

   @Autowired
   public UserTable(UserRestRepository userRepository,
                    UserEditWindow editPopup,
                    MessageSourceAdapter messages) {

//      addBeans(userRepository.loadAll());
      withProperties("cardCode", "firstName", "lastName", "role", "group");
      withColumnHeaders(
            messages.get(this, "cardCode"),
            messages.get(this, "firstName"),
            messages.get(this, "lastName"),
            messages.get(this, "role"),
            messages.get(this, "group")
      );
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setSortContainerPropertyId("lastName");
      setCacheRate(3);
      setColumnCollapsed("cardCode", true);
      withFullWidth();
      setSelectable(true);
      setNullSelectionAllowed(false);
      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            editPopup.edit(click.getRow());
         }
      });
   }
}
