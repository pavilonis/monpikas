package lt.pavilonis.cmm.canteen.views.pupils;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.repositories.MealRepository;
import lt.pavilonis.cmm.canteen.views.pupils.form.MealTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Set;

@UIScope
@SpringComponent
public class PupilEditMealSelectionWindow extends Window {

   @Autowired
   public PupilEditMealSelectionWindow(MessageSourceAdapter messages, MealRepository mealRepository) {

      //TODO set form model (container) instead of getting values from fields manually

      setCaption(messages.get(this, "mealSelectionPopup.caption"));
      setResizable(false);
      setWidth("550px");
      setHeight("540px");

//      vl.setComponentAlignment(buttons, BOTTOM_CENTER);
      MTable<Meal> table = new MealTable(mealRepository.loadAll())
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
