package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.views.components.MealTypeComboBox;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

@UIScope
@SpringComponent
public class UserMealListFilterPanel extends MHorizontalLayout {

   private final TextField textField = new TextField();
   private final ComboBox mealTypeCombo = new MealTypeComboBox();

   public UserMealListFilterPanel() {
      mealTypeCombo.setCaption(null);
      mealTypeCombo.setNullSelectionAllowed(true);
      mealTypeCombo.setValue(null);
      add(
            textField,
            mealTypeCombo,
            new MButton(FontAwesome.FILTER, "Filtruoti", click -> filterAction())
                  .withClickShortcut(KeyCode.ENTER),

            new MButton(FontAwesome.REFRESH, "Valyti", click -> cleanFields())
                  .withClickShortcut(KeyCode.ESCAPE)
      );
      setSpacing(true);
      setMargin(true);
      textField.focus();
   }

   private void filterAction() {
      //TODO
   }

   public void cleanFields() {
      textField.setValue("");
      mealTypeCombo.setValue(null);
   }
}

