package lt.pavilonis.cmm.security.ui;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.SystemUser;
import lt.pavilonis.cmm.common.FieldLayout;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import lt.pavilonis.cmm.security.Role;

import java.util.stream.Stream;

public class SystemUserForm extends FieldLayout<SystemUser> {
   private final TextField name = new ATextField(getClass(), "name");
   private final TextField username = new ATextField(getClass(), "username");
   private final TextField email = new ATextField(getClass(), "email");
   private final CheckBox enabled = new CheckBox(App.translate(this, "enabled"));
   private final PasswordField password = new PasswordField(App.translate(this, "password"));
   private final OneToManyField<Role> authorities = new OneToManyField<>(Role.class, "name");

   public SystemUserForm(boolean existingUser) {

      if (existingUser) {
         password.setVisible(false);
         password.setEnabled(false);
      }

      HorizontalLayout row1 = new HorizontalLayout(username, name);
      HorizontalLayout row2 = new HorizontalLayout(password, email, enabled);
      row2.setComponentAlignment(enabled, Alignment.BOTTOM_CENTER);

      addComponents(row1, row2, authorities);

      Stream.of(username, name, email, password)
            .forEach(field -> field.setWidth(268, Unit.PIXELS));

      setMargin(new MarginInfo(false, false, true, false));
   }
}
