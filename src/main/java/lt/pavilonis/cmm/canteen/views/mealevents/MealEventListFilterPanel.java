package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import lt.pavilonis.cmm.canteen.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

@SpringComponent
@UIScope
public class MealEventListFilterPanel extends MHorizontalLayout {

   private TextField text = new TextField();
   private CheckBox hadDinnerToday = new CheckBox("Pietavo šiandien");

   @Autowired
   private MealService service;

   //TODO add periodStart - periodEnd fields
   public MealEventListFilterPanel(MealEventTable table) {
      add(
            text,
            hadDinnerToday,

            new MButton(FontAwesome.FILTER, "Filtruoti", click -> {
               table.getContainerDataSource();
//               table.getContainerDataSource()
//               removeAllContainerFilters();
//               container.addContainerFilter(filterPanel.getFilter());
            }).withClickShortcut(ShortcutAction.KeyCode.ENTER),

            new MButton(FontAwesome.REFRESH, "Valyti", click -> {
               cleanFields();
               table.reload();
            }).withClickShortcut(ShortcutAction.KeyCode.ESCAPE)
      );
      setComponentAlignment(hadDinnerToday, Alignment.MIDDLE_CENTER);
//      setComponentAlignment(filterButton, Alignment.MIDDLE_RIGHT);
      text.focus();
      setMargin(false);
   }

   public MealEventFilter getFilter() {
      //TODO do something with "service"
      return new MealEventFilter(service, text.getValue(), hadDinnerToday.getValue());
   }

   public void cleanFields() {
      text.setValue("");
      hadDinnerToday.setValue(false);
   }
}