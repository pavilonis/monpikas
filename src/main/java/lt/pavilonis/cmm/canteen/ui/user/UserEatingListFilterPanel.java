package lt.pavilonis.cmm.canteen.ui.user;

import com.vaadin.data.HasValue;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.ATextField;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;

import java.util.Arrays;
import java.util.List;

final class UserEatingListFilterPanel extends FilterPanel<UserEatingFilter> {

   private TextField textField;
   private EnumComboBox<EatingType> eatingTypeComboBox;
   private CheckBox withEatingAssigned;

   @Override
   protected List<HasValue<?>> getFields() {
      // TODO not filtering now - need fix
      eatingTypeComboBox = new EnumComboBox<>(EatingType.class);
      List<HasValue<?>> fields = Arrays.asList(
            textField = new ATextField(getClass(), "name"),
            withEatingAssigned = new ACheckBox(getClass(), "withEatingAssigned").withValue(true)
      );
      eatingTypeComboBox.clear();
      return fields;
   }

   @Override
   public UserEatingFilter getFilter() {
      return new UserEatingFilter(
            eatingTypeComboBox.getValue(),
            textField.getValue(),
            withEatingAssigned.getValue() == Boolean.TRUE
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}

