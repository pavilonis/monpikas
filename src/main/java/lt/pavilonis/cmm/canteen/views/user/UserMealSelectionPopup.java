package lt.pavilonis.cmm.canteen.views.user;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.List;
import java.util.function.Consumer;

public class UserMealSelectionPopup extends MWindow {

   public UserMealSelectionPopup(List<Meal> mealsToSelectFrom,
                                 Consumer<Meal> selectionConsumer,
                                 MessageSourceAdapter messages) {

      setCaption(messages.get(this, "caption"));
      withSize(MSize.size("700px", "490px"));

      MealTable selectionTable = new MealTable(mealsToSelectFrom);
      selectionTable.collapseColumns();
      selectionTable.addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            selectAction(selectionConsumer, click.getRow());
         }
      });

      MHorizontalLayout controls = new MHorizontalLayout(
            new MButton(
                  FontAwesome.PLUS,
                  "Pridėti pasirinktą",
                  click -> selectAction(selectionConsumer, selectionTable.getValue())
            ),
            new MButton(FontAwesome.TIMES, "Uždaryti", click -> close())
      );

      MVerticalLayout layout = new MVerticalLayout(selectionTable, controls)
            .withSize(MSize.FULL_SIZE)
            .expand(selectionTable);

      setContent(layout);
      setModal(true);

      UI.getCurrent().addWindow(this);
   }

   protected void selectAction(Consumer<Meal> mealSelectionConsumer, Meal selectedValue) {
      if (selectedValue != null) {
         mealSelectionConsumer.accept(selectedValue);
      }
      close();
   }
}
