package lt.pavilonis.cmm.canteen.controllers;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repositories.MealRepository;
import lt.pavilonis.cmm.canteen.repositories.PupilDataRepository;
import lt.pavilonis.cmm.canteen.repositories.UserRepository;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.views.settings.MealFormWindow;
import lt.pavilonis.cmm.canteen.views.settings.MealListView;
import lt.pavilonis.cmm.canteen.views.settings.OtherSettingsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static lt.pavilonis.cmm.util.SecurityCheckUtils.hasRole;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Controller
public class ViewController {

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private UserMealService pupilService;

   public void attachComponents(VerticalLayout base) {

      if (hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {

         menu.addItem(" Nustatymai", FontAwesome.WRENCH, selected -> {
            MealListView view1 = new MealListView();
            view1.getContainer().addAll(mealRepository.loadAll());
            view1.setTableClickListener(portionListTableClickListener());
            view1.getControlPanel().addAddListener(portionAddListener(view1));
            view1.getControlPanel().addDeleteListener(portionDeleteListener(view1));
         });
      }
   }

   private ClickListener portionDeleteListener(MealListView view) {
      return click -> {
         Long mealId = (Long) view.getTable().getValue();
         if (mealId == null) {
            show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            List<UserMeal> portionUsers = pupilService.loadByMeal(mealId);
            if (portionUsers.isEmpty()) {
               mealRepository.delete(mealId);
               view.getContainer().removeItem(mealId);
               view.getTable().select(null);
               show("Įrašas pašalintas", TRAY_NOTIFICATION);
            } else {
               show("Porciją priskirta šioms kortelėms:", portionUsers.toString(), ERROR_MESSAGE);
            }
         }
      };
   }

   private ClickListener portionAddListener(MealListView listView) {
      return click -> {
         MealFormWindow w = new MealFormWindow();

         w.addCloseButtonListener(closeClick -> w.close());
         w.addSaveButtonListener(saveClick -> {
            if (w.isValid()) {
               w.commit();
               Meal meal = w.getItemDateSource().getBean();
               mealRepository.saveOrUpdate(meal);
               w.close();
               listView.getContainer().addBean(meal);
               show("Išsaugota", TRAY_NOTIFICATION);
            }
         });
         UI.getCurrent().addWindow(w);
      };
   }

   private ItemClickEvent.ItemClickListener portionListTableClickListener() {
      return event -> {
         Item item = event.getItem();
         if (event.isDoubleClick()) {
            MealFormWindow w = new MealFormWindow(item);

            UI.getCurrent().addWindow(w);
            w.addCloseButtonListener(click -> w.close());
            w.addSaveButtonListener(click -> {
               if (w.isValid()) {
                  w.commit();
                  BeanItem<Meal> bean = w.getItemDateSource();
                  mealRepository.saveOrUpdate(bean.getBean());
                  w.close();
                  show("Išsaugota", TRAY_NOTIFICATION);
               }
            });
         }
      };
   }
}
