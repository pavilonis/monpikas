package lt.pavilonis.cmm.canteen.views.user.form;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

public class UserMealSelectionPopup extends Window {

   public UserMealSelectionPopup(List<Meal> mealsToSelectFrom, MessageSourceAdapter messages) {

      setCaption(messages.get(this, "mealSelectionPopup.caption"));
      setResizable(false);
      setWidth("550px");
      setHeight("540px");

//      vl.setComponentAlignment(buttons, BOTTOM_CENTER);
      MTable<Meal> table = new MealTable(messages, mealsToSelectFrom)
            .withCaption(messages.get(this, "mealSelectionPopup.tableCaption"));

      setContent(
            new MVerticalLayout(
                  table,
                  new MHorizontalLayout(
                        new MButton(FontAwesome.PLUS, "Pridėti pasirinktą", click -> {
                           Meal meal = table.getValue();
//                           @SuppressWarnings("unchecked")
//                           Set<Long> selected = (Set<Long>) selectionWindow.getTable().getValue();
//                           view.addToContainer(mealRepository.load(selected));
                           close();
                        }),
                        new MButton(FontAwesome.TIMES, "Uždaryti", click -> close())
                  )
            )
      );
      setModal(true);
      UI.getCurrent().addWindow(this);
   }
}
