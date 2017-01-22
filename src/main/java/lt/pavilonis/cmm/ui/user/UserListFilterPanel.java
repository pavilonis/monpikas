package lt.pavilonis.cmm.ui.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextField;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
class UserListFilterPanel extends FilterPanel<UserFilter> {

   private TextField textField;
   private ComboBox groupCombo;
   private ComboBox roleCombo;

   @Autowired
   public UserListFilterPanel(UserRestRepository userRepository) {
   }

   @Override
   public UserFilter getFilter() {
      return new UserFilter();
   }

   @Override
   protected List<Field> getFields() {
      return Arrays.asList(
            textField = new MTextField(messages.get(this, "firstLastName")),
            groupCombo = new ComboBox(messages.get(this, "group")),
            roleCombo = new ComboBox(messages.get(this, "role"))
      );
   }

   @Override
   protected Field getFieldToFocus() {
      return textField;
   }
}
