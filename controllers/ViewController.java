package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import lt.pavilonis.monpikas.server.reports.ReportService;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.MealRepository;
import lt.pavilonis.monpikas.server.repositories.PupilRepository;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventListView;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventManualCreateWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilEditMealSelectionWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilsListView;
import lt.pavilonis.monpikas.server.views.reports.ReportGeneratorView;
import lt.pavilonis.monpikas.server.views.settings.MealFormWindow;
import lt.pavilonis.monpikas.server.views.settings.MealListView;
import lt.pavilonis.monpikas.server.views.settings.OtherSettingsView;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.vaadin.server.VaadinService.getCurrent;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Optional.ofNullable;
import static lt.pavilonis.monpikas.server.utils.SecurityCheckUtils.hasRole;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

// TODO Create some kind of structure to manage this

@Controller
public class ViewController {

   private static final Logger LOG = getLogger(ViewController.class.getSimpleName());

   @Value("${pupilEditWindow.PhotoBasePath}")
   private String adbImageBasePath;

   @Value("${images.Extension}")
   private String imageExtension;

   @Value("${images.DefaultPhotoPath}")
   private String noPhotoPath;

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private PupilService pupilService;

   @Autowired
   private PupilRepository pupilRepository;

   @Autowired
   private MealService mealService;

   @Autowired
   private ReportService reportService;

   @Autowired
   private MealEventLogRepository mealEventLogRepository;

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

      menu.addItem(" Ataskaitos", FontAwesome.FILE_EXCEL_O, selected -> {

         ReportGeneratorView view = new ReportGeneratorView(reportService);

         content.removeAllComponents();
         content.addComponent(view);
      });


      if (hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {

         menu.addItem(" Bendras sąrašas", FontAwesome.CHILD, selected -> {

            PupilsListView view = new PupilsListView();
            view.getContainer().addAll(pupilService.getMergedList());
            view.setTableClickListener(pulilListTableClickListener());

            content.removeAllComponents();
            content.addComponent(view);
         });

         menu.addItem(" Nustatymai", FontAwesome.WRENCH, selected -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setSizeFull();

            MealListView view1 = new MealListView();
            view1.getContainer().addAll(mealRepository.findAll());
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
            List<Pupil> portionUsers = pupilRepository.findPortionUsers(mealId);
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
               mealRepository.save(meal);
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
                  mealRepository.save(bean.getBean());
                  w.close();
                  show("Išsaugota", TRAY_NOTIFICATION);
               }
            });
         }
      };
   }

   private ItemClickEvent.ItemClickListener pulilListTableClickListener() {
      return event -> {

         if (!event.isDoubleClick())
            return;

         @SuppressWarnings("unchecked")
         PupilDto dto = ((BeanItem<PupilDto>) event.getItem()).getBean();
         Pupil pupil = pupilService.infoByCardId(dto.getCardId()).orElse(new Pupil(dto.getCardId()));

         Optional<Date> lastMeal = mealService.lastMealEvent(dto.getCardId());
         Image photo = getImage(dto.getAdbId());
         PupilEditWindow view = new PupilEditWindow(pupil, dto, photo, lastMeal);

         view.addAddMealButtonListener(click -> {
            PupilEditMealSelectionWindow selectionWindow = new PupilEditMealSelectionWindow();
            selectionWindow.getContainer().addAll(mealRepository.findAll());
            selectionWindow.addCloseButtonListener(close -> selectionWindow.close());
            selectionWindow.addSaveButtonListener(select -> {
               @SuppressWarnings("unchecked")
               Collection<Long> selected = (Collection<Long>) selectionWindow.getTable().getValue();
               view.addToContainer(mealRepository.findAll(selected));
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

                  @SuppressWarnings("unchecked")
                  Collection<Long> mealIds = (Collection<Long>) view.getTable().getItemIds();

                  pupil.getMeals().clear();
                  pupil.getMeals().addAll(mealRepository.findAll(mealIds));

                  view.commit();
                  pupilService.saveOrUpdate(pupil);
                  //TODO reload record from db
//                  dto.setComment(ofNullable(pupil.getComment()));  //manual value refresh in list?
                  Table tbl = (Table) event.getSource();
                  tbl.refreshRowCache();
                  view.close();
                  show("Išsaugota", TRAY_NOTIFICATION);
               });

         UI.getCurrent().addWindow(view);
      };
   }

   private Image getImage(long id) {
      String remoteImgUrl = adbImageBasePath + id + imageExtension;
      Resource resource;
      if (remoteImageExists(remoteImgUrl)) {
         resource = new ExternalResource(remoteImgUrl);
      } else {
         resource = new FileResource(new File(
               getCurrent().getBaseDirectory().getAbsolutePath() + File.separator + noPhotoPath
         ));
      }
      return new Image(null, resource);
   }

   private boolean remoteImageExists(String url) {
      try {
         URL u = new URL(url);
         u.getPath();
         HttpURLConnection http = (HttpURLConnection) u.openConnection();
         http.setInstanceFollowRedirects(false);
         http.setRequestMethod("HEAD");
         http.connect();
         return (http.getResponseCode() == HTTP_OK);
      } catch (Exception e) {
         LOG.error("Image request error: " + e);
         return false;
      }
   }

   private ClickListener mealAddEventListener(MealEventListView listView) {
      return click -> {
         if (!hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {
            show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
            return;
         }
         MealEventManualCreateWindow w = new MealEventManualCreateWindow();
         w.getContainer().addAll(pupilService.getMergedWithPortions());
         w.addCloseButtonListener(closeClick -> w.close());
         w.addSaveButtonListener(saveClick -> {
            Long id = (Long) w.getTable().getValue();
            MealType type = w.getEventType();
            if (valid(id, w.getDate(), type)) {
               PupilDto dto = pupilService.getByCardId(id).get();

               BigDecimal price = dto.getMeals().stream()
                     .filter(portion -> portion.getType() == type)
                     .findFirst()
                     .get()
                     .getPrice();

               MealEventLog event = new MealEventLog(
                     dto.getCardId(),
                     dto.getFirstName() + " " + dto.getLastName(),
                     dto.getGrade(),
                     w.getDate(),
                     price,
                     type,
                     dto.getPupilType()
               );
               mealEventLogRepository.save(event);
               listView.getContainer().addBean(event);
               w.close();
               show("Išsaugota", TRAY_NOTIFICATION);
            }
         });
         UI.getCurrent().addWindow(w);
      };
   }

   private boolean valid(Long id, Date date, MealType type) {
      if (id == null) {
         show("Nepasirinktas mokinys", WARNING_MESSAGE);
         return false;
      } else if (date == null) {
         show("Nenurodyta data", WARNING_MESSAGE);
         return false;
      } else if (!pupilService.portionAssigned(id, type)) {
         show("Mokinys neturi leidimo šio tipo maitinimuisi", ERROR_MESSAGE);
         return false;
      } else if (!pupilService.canHaveMeal(id, date, type)) {
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
            mealEventLogRepository.delete(id);
            listView.getContainer().removeItem(id);
            listView.getTable().select(null);
            show("Įrašas pašalintas", TRAY_NOTIFICATION);
         }
      };
   }
}
