package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.views.component.EnumComboBox;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

@UIScope
@SpringComponent
public class UserMealListFilterPanel extends MHorizontalLayout {

   public UserMealListFilterPanel() {
      TextField textField = new TextField();
      EnumComboBox<MealType> mealTypeComboBox = new EnumComboBox<>(MealType.class);
      mealTypeComboBox.setCaption(null);
      mealTypeComboBox.setNullSelectionAllowed(true);
      mealTypeComboBox.setValue(null);
      add(
            textField,
            mealTypeComboBox,
            new MButton(FontAwesome.FILTER, "Filtruoti", click -> filterAction(textField.getValue()))
                  .withClickShortcut(KeyCode.ENTER),

            new MButton(FontAwesome.REFRESH, "Valyti", click -> {
               textField.clear();
               mealTypeComboBox.clear();
            }).withClickShortcut(KeyCode.ESCAPE)
      );
      setSpacing(true);
      setMargin(true);
      textField.focus();
   }

   private void filterAction(String value) {
      //TODO
   }
}

