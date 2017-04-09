package lt.pavilonis.cmm.canteen.ui.user;

import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.field.ACheckBox;
import lt.pavilonis.cmm.common.field.EnumComboBox;

import java.util.Arrays;
import java.util.List;

@UIScope
@SpringComponent
public class UserMealListFilterPanel extends FilterPanel<UserMealFilter> {

   private TextField textField;
   private EnumComboBox<MealType> mealTypeComboBox;
   private CheckBox withMealAssigned;

   @Override
   protected List<HasValue<?>> getFields() {
      // TODO not filtering now - need fix
      mealTypeComboBox = new EnumComboBox<>(MealType.class);
      List<HasValue<?>> fields = Arrays.asList(
            textField = new TextField(messages.get(this, "name")),
            withMealAssigned = new ACheckBox(this.getClass(), "withMealAssigned").withValue(true)
      );
      mealTypeComboBox.clear();
      return fields;
   }

   @Override
   public UserMealFilter getFilter() {
      return new UserMealFilter(
            mealTypeComboBox.getValue(),
            textField.getValue(),
            withMealAssigned.getValue() == Boolean.TRUE
      );
   }

   @Override
   protected AbstractField<?> getFieldToFocus() {
      return textField;
   }
}

