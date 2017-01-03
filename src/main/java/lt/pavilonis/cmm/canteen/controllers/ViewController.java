package lt.pavilonis.cmm.canteen.controllers;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.domain.PupilLocalData;
import lt.pavilonis.cmm.canteen.reports.ReportService;
import lt.pavilonis.cmm.canteen.repositories.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.repositories.MealRepository;
import lt.pavilonis.cmm.canteen.repositories.PupilDataRepository;
import lt.pavilonis.cmm.canteen.repositories.UserRepository;
import lt.pavilonis.cmm.canteen.service.MealService;
import lt.pavilonis.cmm.canteen.service.PupilService;
import lt.pavilonis.cmm.canteen.views.mealevents.MealEventListView;
import lt.pavilonis.cmm.canteen.views.mealevents.MealEventManualCreateForm;
import lt.pavilonis.cmm.canteen.views.pupils.PupilEditMealSelectionWindow;
import lt.pavilonis.cmm.canteen.views.pupils.PupilEditWindow;
import lt.pavilonis.cmm.canteen.views.pupils.PupilsListView;
import lt.pavilonis.cmm.canteen.views.reports.ReportGeneratorView;
import lt.pavilonis.cmm.canteen.views.settings.MealFormWindow;
import lt.pavilonis.cmm.canteen.views.settings.MealListView;
import lt.pavilonis.cmm.canteen.views.settings.OtherSettingsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static lt.pavilonis.cmm.util.SecurityCheckUtils.hasRole;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

// TODO Create some kind of structure to manage this

@Controller
public class ViewController {

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private PupilService pupilService;

   @Autowired
   private PupilDataRepository pupilDataRepository;

   @Autowired
   private MealService mealService;

   @Autowired
   private MealEventLogRepository eventLogs;

   public void attachComponents(VerticalLayout base) {

      VerticalLayout content = new VerticalLayout();
      content.setSizeFull();

      MenuBar menu = new MenuBar();

      menu.addItem(" Žurnalas", FontAwesome.CUTLERY, selected -> {

         MealEventListView view = new MealEventListView();
         view.getContainer().addAll(mealService.getDinnerEventList());
         view.getControlPanel().addAddListener(mealAddEventListener(view));
         view.getControlPanel().addDeleteListener(mealDeleteEventListener(view));

         content.removeAllComponents();
         content.addComponent(view);
      });

      if (hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {

         menu.addItem(" Bendras sąrašas", FontAwesome.CHILD, selected -> {

            PupilsListView view = new PupilsListView();
            view.getContainer().addAll(pupilService.loadAll());
            view.getContainer().sort(new Object[]{"firstName", "lastName"}, new boolean[]{true, true});
            view.setTableClickListener(pupilListTableClickListener());

            content.removeAllComponents();
            content.addComponent(view);
         });

         menu.addItem(" Nustatymai", FontAwesome.WRENCH, selected -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setSizeFull();

            MealListView view1 = new MealListView();
            view1.getContainer().addAll(mealRepository.loadAll());
            view1.setTableClickListener(portionListTableClickListener());
            view1.getControlPanel().addAddListener(portionAddListener(view1));
            view1.getControlPanel().addDeleteListener(portionDeleteListener(view1));

            OtherSettingsView view2 = new OtherSettingsView();
            view2.addSaveButtonClickListener(click -> show("Funkcionalumas nerealizuotas", WARNING_MESSAGE));
            hl.addComponents(view1, view2);
            content.removeAllComponents();
            content.addComponent(hl);
         });
      }

      MenuItem userItem = menu.addItem(" " + getContext().getAuthentication().getName(), FontAwesome.USER, null);
      userItem.addItem(" Atsijungti", FontAwesome.POWER_OFF, selected -> {
         content.removeAllComponents();
         getContext().setAuthentication(null);
         UI.getCurrent().close();
         show("Atsijungta", HUMANIZED_MESSAGE);
      });

      base.addComponents(menu, content);
      base.setExpandRatio(content, 1f);
   }

   private ClickListener portionDeleteListener(MealListView view) {
      return click -> {
         Long mealId = (Long) view.getTable().getValue();
         if (mealId == null) {
            show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            List<Pupil> portionUsers = pupilService.loadByMeal(mealId);
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

   private ItemClickEvent.ItemClickListener pupilListTableClickListener() {
      return event -> {

         if (!event.isDoubleClick())
            return;

         String cardCode = (String) event.getItemId();
         PupilEditWindow view = new PupilEditWindow(
               pupilDataRepository.load(cardCode).orElse(new PupilLocalData(cardCode)),
               userRepository.load(cardCode).get(),
               mealService.lastMealEvent(cardCode)
         );

         view.addAddMealButtonListener(click -> {
            PupilEditMealSelectionWindow selectionWindow = new PupilEditMealSelectionWindow();
            selectionWindow.getContainer().addAll(mealRepository.loadAll());
            selectionWindow.addCloseButtonListener(close -> selectionWindow.close());
            selectionWindow.addSaveButtonListener(select -> {
               @SuppressWarnings("unchecked")
               Set<Long> selected = (Set<Long>) selectionWindow.getTable().getValue();
               view.addToContainer(mealRepository.load(selected));
               selectionWindow.close();
            });
            UI.getCurrent().addWindow(selectionWindow);
         });

         view.addRemoveMealButtonListener(click -> {
            @SuppressWarnings("unchecked")
            Collection<Long> selected = (Collection<Long>) view.getTable().getValue();
            view.removeFromContainer(selected);
         });

         view.addCloseButtonListener(closeBtnClick -> view.close());
         view.addSaveButtonListener(
               saveBtnClick -> {

                  if (!view.isValid())
                     return;

                  Collection<?> itemIds = view.getTable().getItemIds();

                  PupilLocalData model = view.getModel();
                  model.setMeals(new HashSet<>(mealRepository.load(itemIds)));

                  view.commit();
                  pupilDataRepository.saveOrUpdate(model);
                  Table tbl = (Table) event.getSource();

                  @SuppressWarnings("unchecked")
                  BeanContainer<String, Pupil> container =
                        (BeanContainer<String, Pupil>) tbl.getContainerDataSource();

                  int index = container.indexOfId(cardCode);
                  container.removeItem(cardCode);
                  container.addBeanAt(index, pupilService.load(cardCode).orElseThrow(IllegalStateException::new));
                  view.close();
                  show("Išsaugota", TRAY_NOTIFICATION);
               });

         UI.getCurrent().addWindow(view);
      };
   }

   private ClickListener mealAddEventListener(MealEventListView listView) {
      return click -> {
         if (!hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {
            show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
            return;
         }
         MealEventManualCreateForm form = new MealEventManualCreateForm();
         form.getContainer().addAll(pupilService.loadWithMealAssigned());
         form.addCloseButtonListener(closeClick -> form.close());
         form.addSaveButtonListener(saveClick -> {
            String cardCode = (String) form.getTable().getValue();
            MealType type = form.getEventType();
            if (valid(cardCode, form.getDate(), type)) {
               Pupil pupil = pupilService.find(cardCode).get();

               BigDecimal price = pupil.getMeals().stream()
                     .filter(portion -> portion.getType() == type)
                     .findFirst()
                     .get()
                     .getPrice();

               MealEventLog log = eventLogs.save(
                     pupil.getCardCode(),
                     pupil.name(),
                     pupil.getGrade(),
                     price,
                     type,
                     pupil.getType()
               );
               listView.getContainer().addBean(log);
               form.close();
               show("Išsaugota", TRAY_NOTIFICATION);
            }
         });
         UI.getCurrent().addWindow(form);
      };
   }

   private boolean valid(String cardCode, Date date, MealType mealType) {
      if (cardCode == null) {
         show("Nepasirinktas mokinys", WARNING_MESSAGE);
         return false;
      } else if (date == null) {
         show("Nenurodyta data", WARNING_MESSAGE);
         return false;
      } else if (!pupilService.portionAssigned(cardCode, mealType)) {
         show("Mokinys neturi leidimo šio tipo maitinimuisi", ERROR_MESSAGE);
         return false;
      } else if (!pupilService.canHaveMeal(cardCode, date, mealType)) {
         show("Viršijamas nurodytos dienos maitinimosi limitas", ERROR_MESSAGE);
         return false;
      } else {
         return true;
      }
   }

   private ClickListener mealDeleteEventListener(MealEventListView listView) {
      return click -> {
         if (!hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {
            show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
            return;
         }
         Long id = (Long) listView.getTable().getValue();
         if (id == null) {
            show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            eventLogs.delete(id);
            listView.getContainer().removeItem(id);
            listView.getTable().select(null);
            show("Įrašas pašalintas", TRAY_NOTIFICATION);
         }
      };
   }
}
