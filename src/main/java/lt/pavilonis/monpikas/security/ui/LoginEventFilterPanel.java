package lt.pavilonis.monpikas.security.ui;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import lt.pavilonis.monpikas.common.field.ACheckBox;
import lt.pavilonis.monpikas.common.ui.filter.PeriodTextFilterPanel;
import lt.pavilonis.monpikas.security.LoginEventFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoginEventFilterPanel extends PeriodTextFilterPanel<LoginEventFilter> {

   private CheckBox success;
   private CheckBox logout;

   @Override
   public LoginEventFilter getFilter() {
      return LoginEventFilter.builder()
            .periodStart(getPeriodStart().getValue())
            .periodEnd(getPeriodEnd().getValue())
            .text(getText().getValue())
            .success(success.getValue())
            .logout(logout.getValue())
            .build();
   }

   @Override
   protected List<HasValue<?>> getFields() {
      List<HasValue<?>> fields = new ArrayList<>(super.getFields());
      fields.add(success = new ACheckBox(LoginEventFilterPanel.class, "success"));
      fields.add(logout = new ACheckBox(LoginEventFilterPanel.class, "logout"));
      return fields;
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return getText();
   }

   @Override
   protected void setDefaultValues() {
      getPeriodStart().setValue(LocalDate.now().minusDays(1));
      success.setValue(false);
      logout.setValue(false);
   }
}
