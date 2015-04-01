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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.reports.ReportService;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PortionService;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventListView;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventManualCreateWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilsListView;
import lt.pavilonis.monpikas.server.views.reports.ReportGeneratorView;
import lt.pavilonis.monpikas.server.views.settings.OtherSettingsView;
import lt.pavilonis.monpikas.server.views.settings.PortionFormWindow;
import lt.pavilonis.monpikas.server.views.settings.PortionListView;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static com.vaadin.server.VaadinService.getCurrent;
import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Optional.ofNullable;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.utils.SecurityCheckUtils.hasRole;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

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
   private PupilService pupilService;

   @Autowired
   private MealService mealService;

   @Autowired
   private PortionService portionService;

   @Autowired
   private ReportService reportService;

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

            PortionListView view1 = new PortionListView();
            view1.getContainer().addAll(portionService.getAll());
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

   private ClickListener portionDeleteListener(PortionListView view) {
      return click -> {
         Long id = (Long) view.getTable().getValue();
         if (id == null) {
            show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            List<PupilInfo> portionUsers = pupilService.findFirstByPortionId(id);
            if (portionUsers.isEmpty()) {
               portionService.delete(id);
               view.getContainer().removeItem(id);
               view.getTable().select(null);
               show("Įrašas pašalintas", TRAY_NOTIFICATION);
            } else {
               show("Porciją priskirta šioms kortelėms:", portionUsers.toString(), ERROR_MESSAGE);
            }
         }
      };
   }

   private ClickListener portionAddListener(PortionListView listView) {
      return click -> {
         PortionFormWindow w = new PortionFormWindow();

         w.addCloseButtonListener(closeClick -> w.close());
         w.addSaveButtonListener(saveClick -> {
            if (w.isValid()) {
               w.commit();
               Portion portion = w.getItemDateSource().getBean();
               portionService.save(portion);
               w.close();
               listView.getContainer().addBean(portion);
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
            PortionFormWindow w = new PortionFormWindow(item);
            UI.getCurrent().addWindow(w);
            w.addCloseButtonListener(click -> w.close());
            w.addSaveButtonListener(click -> {
               if (w.isValid()) {
                  w.commit();
                  BeanItem<Portion> bean = w.getItemDateSource();
                  portionService.save(bean.getBean());
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

         BeanItem<AdbPupilDto> item = (BeanItem<AdbPupilDto>) event.getItem();
         AdbPupilDto dto = item.getBean();
         PupilInfo info = pupilService.infoByCardId(dto.getCardId()).orElse(new PupilInfo(dto.getCardId()));
         PupilEditWindow view = new PupilEditWindow(
               info,
               dto,
               getImage(dto.getAdbId()),
               mealService.lastMealEvent(dto.getCardId()),
               portionService.getAll());

         view.addCloseButtonListener(closeBtnClick -> view.close());
         view.addSaveButtonListener(
               saveBtnClick -> {

                  if (!view.isValid())
                     return;

                  view.commit();
                  pupilService.saveOrUpdate(info);
                  dto.setBreakfastPortion(ofNullable(info.getBreakfastPortion()));
                  dto.setDinnerPortion(ofNullable(info.getDinnerPortion()));
                  dto.setGrade(ofNullable(info.getGrade()));
                  dto.setComment(ofNullable(info.getComment()));
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
         w.getContainer().addAll(pupilService.getMergedMealAllowedList());
         w.addCloseButtonListener(closeClick -> w.close());
         w.addSaveButtonListener(saveClick -> {
            Long id = (Long) w.getTable().getValue();
            PortionType type = w.getPortionType();
            if (valid(id, w.getDate(), type)) {
               AdbPupilDto dto = pupilService.getByCardId(id).get();

               BigDecimal price = type == BREAKFAST
                     ? dto.getBreakfastPortion().get().getPrice()
                     : dto.getDinnerPortion().get().getPrice();

               MealEvent event = new MealEvent(
                     dto.getCardId(),
                     dto.getFirstName() + " " + dto.getLastName(),
                     dto.getGrade().orElse(""),
                     w.getDate(),
                     price,
                     type
               );
               mealService.saveMealEvent(event);
               listView.getContainer().addBean(event);
               w.close();
               show("Išsaugota", TRAY_NOTIFICATION);
            }
         });
         UI.getCurrent().addWindow(w);
      };
   }

   private boolean valid(Long id, Date date, PortionType type) {
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
            Notification.show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            mealService.deleteMealEvent(id);
            listView.getContainer().removeItem(id);
            listView.getTable().select(null);
            show("Įrašas pašalintas", TRAY_NOTIFICATION);
         }
      };
   }
}
