package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.ui.Button.ClickListener;

@SpringComponent
@UIScope
public class MealEventListFilterPanel extends HorizontalLayout {

   private TextField text = new TextField();
   private CheckBox hadDinnerToday = new CheckBox("Pietavo šiandien");
   private Button filterButton = new Button("Filtruoti", FontAwesome.FILTER);
   private Button cancelFilterButton = new Button("Valyti", FontAwesome.REFRESH);

   @Autowired
   private MealService service;

   public MealEventListFilterPanel() {
      addComponents(text, hadDinnerToday, filterButton, cancelFilterButton);
      setSpacing(true);
      setMargin(true);
      setComponentAlignment(hadDinnerToday, Alignment.MIDDLE_CENTER);
      setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
      filterButton.addShortcutListener(new ShortcutListener("search", ShortcutAction.KeyCode.ENTER, null) {
         @Override
         public void handleAction(Object sender, Object target) {
            filterButton.click();
         }
      });
      text.focus();
   }

   public void addFilterButtonListener(ClickListener listener) {
      filterButton.addClickListener(listener);
   }

   public MealEventFilter getFilter() {
      return new MealEventFilter(service, text.getValue(), hadDinnerToday.getValue());
   }

   public void addCancelFilterButtonListener(ClickListener listener) {
      cancelFilterButton.addClickListener(listener);
   }

   public void cleanFields() {
      text.setValue("");
      hadDinnerToday.setValue(false);
   }
}