package lt.pavilonis.cmm.ui.security;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.OneToManyField;
import org.springframework.security.core.GrantedAuthority;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;

import java.util.stream.Stream;

public class SecurityUserFormView extends FormView<SecurityUser> {
   private final TextField name = new ATextField(this.getClass(), "name");
   private final TextField username = new ATextField(this.getClass(), "username");
   private final TextField email = new ATextField(this.getClass(), "email");
   private final CheckBox enabled = new CheckBox(App.translate(this, "enabled"));
   private final OneToManyField<GrantedAuthority> authorities = new OneToManyField<>(GrantedAuthority.class);

   public SecurityUserFormView() {
      add(
            new MHorizontalLayout(username, name)
                  .withMargin(false),

            new MHorizontalLayout(email, enabled)
                  .withMargin(false)
                  .withAlign(enabled, Alignment.BOTTOM_CENTER),
            authorities
      );

      Stream.of(username, name, email)
            .forEach(field -> field.setWidth("268px"));
      authorities.setTableHeight(256, Unit.PIXELS);
      withMargin(new MMarginInfo(false, false, true, false));
   }
}
