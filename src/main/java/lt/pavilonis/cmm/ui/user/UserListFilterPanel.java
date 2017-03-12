package lt.pavilonis.cmm.ui.user;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.field.ATextField;

import java.util.Arrays;
import java.util.List;

@SpringComponent
@UIScope
class UserListFilterPanel extends FilterPanel<UserFilter> {

   private TextField textField;
   private ComboBox groupCombo;
   private ComboBox roleCombo;

   @Override
   public UserFilter getFilter() {
      return new UserFilter(
            textField.getValue(),
            (String) roleCombo.getValue(),
            (String) groupCombo.getValue()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> fields = Arrays.asList(
            textField = new ATextField(this.getClass(), "firstLastName"),
            groupCombo = new ComboBox(messages.get(this, "group")),
            roleCombo = new ComboBox(messages.get(this, "role"))
      );
      groupCombo.setVisible(false);
      roleCombo.setVisible(false);
      return fields;
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
