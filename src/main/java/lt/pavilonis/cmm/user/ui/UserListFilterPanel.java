package lt.pavilonis.cmm.user.ui;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;

import java.util.Arrays;
import java.util.List;

final class UserListFilterPanel extends FilterPanel<UserFilter> {

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
            groupCombo = new ComboBox(App.translate(this, "group")),
            roleCombo = new ComboBox(App.translate(this, "role"))
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
