package lt.pavilonis.cmm.school.user;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;

import java.util.List;

final class UserListFilterPanel extends FilterPanel<UserFilter> {

   private final String preferredRole = App.translate(this, "preferredRole");
   private TextField textField;
   private ComboBox<String> groupCombo;
   private ComboBox<String> roleCombo;
   private CheckBox withFirstLastNameCheckBox;

   public UserListFilterPanel(List<String> roles, List<String> groups) {
      roleCombo.setItems(roles);
      groupCombo.setItems(groups);

      roles.stream()
            .filter(preferredRole::equals)
            .findFirst()
            .ifPresent(roleCombo::setValue);
   }

   @Override
   public UserFilter getFilter() {
      return new UserFilter(
            textField.getValue(),
            roleCombo.getValue(),
            groupCombo.getValue(),
            withFirstLastNameCheckBox.getValue()
      );
   }

   @Override
   protected List<HasValue<?>> getFields() {
      return List.of(
            textField = new ATextField(this.getClass(), "firstLastName"),
            roleCombo = new ComboBox<>(App.translate(this, "role")),
            groupCombo = new ComboBox<>(App.translate(this, "group")),
            withFirstLastNameCheckBox = new CheckBox(App.translate(this, "withFirstLastName"), true)
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}
