package lt.pavilonis.cmm.ui.security;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.SecurityUser;
import lt.pavilonis.cmm.common.FormView;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.common.field.OneToManyField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;

import javax.management.relation.Role;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SecurityUserFormView extends FormView<SecurityUser> {
   private final TextField name = new TextField(App.translate(this, "name"));
   private final TextField username = new TextField(App.translate(this, "username"));
   private final TextField email = new TextField(App.translate(this, "email"));
   private final CheckBox enabled = new CheckBox(App.translate(this, "enabled"));
   private final OneToManyField<Role> roles = new OneToManyField<Role>(Role.class);
//      @Override
//      protected List<String> getSelectorEntities() {
//         return Arrays.asList("not", "ready", "yet");
//      }
////TODO maybe set different container
//      @Override
//      protected ListTable<String> createTable(Class<String> type) {
//         return new ListTable<String>(type) {
//            @Override
//            protected List<String> collectProperties(Class<String> type) {
//               return Collections.singletonList("value");
//            }
//         };
//      }
//   };

   public SecurityUserFormView() {
      add(
            new MHorizontalLayout(username, name)
                  .withMargin(false),

            new MHorizontalLayout(email, enabled)
                  .withMargin(false)
                  .withAlign(enabled, Alignment.BOTTOM_CENTER),
            roles
      );

      Stream.of(username, name, email)
            .forEach(field -> field.setWidth("268px"));
      withMargin(new MMarginInfo(false, false, true, false));
   }
}
