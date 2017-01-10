package lt.pavilonis.cmm.canteen.views.setting;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.user.form.MealTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;

@SpringComponent
@UIScope
public class MealListView extends MVerticalLayout {

   //TODO add popup dialog "are you sure..."

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private UserMealService userMealService;

   @Autowired
   public MealListView(MessageSourceAdapter messages, MealRepository mealRepository) {
      this.mealRepository = mealRepository;
      MealTable mealsTable = new MealTable(mealRepository.loadAll());
      mealsTable.addRowClickListener(click -> {

      });
      add(
            new Label(messages.get(this, "header")),
            mealsTable,
            new TableControlPanel(
                  event -> actionAdd(mealsTable),
                  event -> actionDelete(mealsTable)
            )
      );
      expand(mealsTable);
   }

   private void actionDelete(MealTable mealsTable) {
      Meal selected = mealsTable.getValue();
      if (selected == null) {
         show("Niekas nepasirinkta", WARNING_MESSAGE);
      } else {
         List<UserMeal> portionUsers = userMealService.loadByMeal(selected.getId());
         if (portionUsers.isEmpty()) {
            mealRepository.delete(selected.getId());
            mealsTable.removeItem(selected);
            mealsTable.select(null);
            show("Įrašas pašalintas", TRAY_NOTIFICATION);
         } else {
            show("Porciją priskirta šioms kortelėms:", portionUsers.toString(), ERROR_MESSAGE);
         }
      }
   }

   private void actionAdd(MealTable mealsTable) {
      MealForm form = new MealForm();

      form.addCloseButtonListener(closeClick -> form.close());
      form.addSaveButtonListener(saveClick -> {
         if (form.isValid()) {
            form.commit();
            Meal meal = form.getItemDateSource().getBean();
            Meal persisted = mealRepository.saveOrUpdate(meal);
            form.close();
            mealsTable.addBeans(persisted);
            show("Išsaugota", TRAY_NOTIFICATION);
         }
      });
      UI.getCurrent().addWindow(form);
   }
}
