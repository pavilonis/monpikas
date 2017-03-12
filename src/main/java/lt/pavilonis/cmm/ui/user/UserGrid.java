package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.ui.user.form.UserEditWindow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringComponent
@UIScope
class UserGrid extends ListGrid<UserRepresentation> {

   @Autowired
   public UserGrid(UserEditWindow editPopup) {
      super(UserRepresentation.class);

      addSelectionListener(event ->
                  event.getFirstSelectedItem()
                        .ifPresent(editPopup::edit)
      );
   }

   @Override
   protected List<String> getProperties(Class<UserRepresentation> type) {
      return Arrays.asList("cardCode", "firstName", "lastName", "role", "group");
   }

   @Override
   protected void customize() {
      sort("lastName");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("cardCode");
   }
}
