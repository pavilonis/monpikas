package lt.pavilonis.cmm.school.user;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;

import java.util.List;

final class UserListFilterPanel extends FilterPanel<UserFilter> {

   private TextField textField;
   private ComboBox<String> groupCombo;
   private ComboBox<String> roleCombo;

   public UserListFilterPanel(List<String> roles, List<String> groups) {
      roleCombo.setItems(roles);
      groupCombo.setItems(groups);

      String preferredRole = App.translate(this, "preferredRole");
      roles.stream()
            .filter(preferredRole::equals)
            .findFirst()
            .ifPresent(roleCombo::setValue);
   }

   @Override
   public UserFilter getFilter() {
      return new UserFilter(textField.getValue(), roleCombo.getValue(), groupCombo.getValue());
   }

   @Override
   protected List<HasValue<?>> getFields() {
      return List.of(
            textField = new ATextField(this.getClass(), "firstLastName"),
            roleCombo = new ComboBox<>(App.translate(this, "role")),
            groupCombo = new ComboBox<>(App.translate(this, "group"))
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
