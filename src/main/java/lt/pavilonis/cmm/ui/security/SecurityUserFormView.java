package lt.pavilonis.cmm.ui.security;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

public class SecurityUserFormView extends FormView<SecurityUser> {
   private final TextField name = new ATextField(this.getClass(), "name");
   private final TextField username = new ATextField(this.getClass(), "username");
   private final TextField email = new ATextField(this.getClass(), "email");
   private final CheckBox enabled = new CheckBox(App.translate(this, "enabled"));
   private final OneToManyField<GrantedAuthority> authorities = new OneToManyField<>(GrantedAuthority.class);

   public SecurityUserFormView() {

      HorizontalLayout row1 = new HorizontalLayout(username, name);
      HorizontalLayout row2 = new HorizontalLayout(email, enabled);
      row2.setComponentAlignment(enabled, Alignment.BOTTOM_CENTER);

      addComponents(row1, row2, authorities);

      Stream.of(username, name, email)
            .forEach(field -> field.setWidth(268, Unit.PIXELS));

      authorities.setTableHeight(256, Unit.PIXELS);
      setMargin(new MarginInfo(false, false, true, false));
   }
}
