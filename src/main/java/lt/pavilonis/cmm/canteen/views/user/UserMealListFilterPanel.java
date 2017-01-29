package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.common.field.EnumComboBox;
import lt.pavilonis.cmm.common.FilterPanel;
import org.vaadin.viritin.fields.MCheckBox;

import java.util.Arrays;
import java.util.List;

@UIScope
@SpringComponent
public class UserMealListFilterPanel extends FilterPanel<UserMealFilter> {

   private TextField textField;
   private EnumComboBox<MealType> mealTypeComboBox;
   private CheckBox withMealAssigned;

   @Override
   protected List<Field> getFields() {
      // TODO not filtering now - need fix
      mealTypeComboBox = new EnumComboBox<>(MealType.class);
      List<Field> fields = Arrays.asList(
            textField = new TextField(messages.get(this, "name")),
            withMealAssigned = new MCheckBox(messages.get(this, "withMealAssigned"), true)
      );
      mealTypeComboBox.setValue(null);
      textField.setImmediate(true);
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
   protected Field getFieldToFocus() {
      return textField;
   }
}

