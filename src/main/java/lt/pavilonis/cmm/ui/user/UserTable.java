package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;
import lt.pavilonis.cmm.ui.user.form.UserEditWindow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@SpringComponent
@UIScope
class UserTable extends ListTable<UserRepresentation> {

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

      setSortContainerPropertyId("lastName");

      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            editPopup.edit(click.getRow());
         }
      });
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("cardCode");
   }
}
