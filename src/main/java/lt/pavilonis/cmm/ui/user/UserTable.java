package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.domain.UserRepresentation;
import lt.pavilonis.cmm.ui.user.form.UserEditWindow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringComponent
@UIScope
class UserTable extends ListTable<UserRepresentation> {

   @Autowired
   public UserTable(UserEditWindow editPopup) {
      super(UserRepresentation.class);

      addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            editPopup.edit(click.getRow());
         }
      });
   }

   @Override
   protected List<String> getProperties() {
      return Arrays.asList("cardCode", "firstName", "lastName", "role", "group");
   }

   @Override
   protected void customize(MessageSourceAdapter messageSource) {
      setSortContainerPropertyId("lastName");
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("cardCode");
   }
}
