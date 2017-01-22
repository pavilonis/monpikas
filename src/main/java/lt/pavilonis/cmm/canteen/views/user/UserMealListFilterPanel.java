package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import lt.pavilonis.cmm.common.FilterPanel;

import java.util.Arrays;
import java.util.List;

@UIScope
@SpringComponent
public class UserMealListFilterPanel extends FilterPanel<UserMealFilter> {

   private TextField textField;
   private EnumComboBox<MealType> mealTypeComboBox;

   @Override
   protected List<Field> getFields() {
      List<Field> fields = Arrays.asList(
            mealTypeComboBox = new EnumComboBox<>(MealType.class),
            textField = new TextField(messages.get(this, "name"))
      );
      mealTypeComboBox.setValue(null);
      textField.setImmediate(true);
      return fields;
   }

   @Override
   public UserMealFilter getFilter() {
      return new UserMealFilter(
            mealTypeComboBox.getValue(),
            textField.getValue()
      );
   }

   @Override
   protected Field getFieldToFocus() {
      return textField;
   }
}

