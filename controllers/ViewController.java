package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventListView;
import lt.pavilonis.monpikas.server.views.mealevents.MealEventManualCreateWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.pupils.PupilsListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

@Controller
public class ViewController {

   @Value("${PupilEditWindow.PhotoBasePath}")
   private String adbImageBasePath;

   @Value("${Images.Extension}")
   private String imageExtension;

   @Value("${Images.DefaultPhotoPath}")
   private String noPhotoPath;

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MealService mealService;

   public TabSheet createAdbPulilListView() {
      PupilsListView pupilsView = new PupilsListView();
      pupilsView.getContainer().addAll(pupilService.getMergedList());
      pupilsView.setTableClickListener(newPulilListTableClickListener());

      MealEventListView mealView = new MealEventListView();
      mealView.getContainer().addAll(mealService.getDinnerEventList());
      mealView.getControlPanel().addAddListener(newAddMealEventListener(mealView));
      mealView.getControlPanel().addDeleteListener(newDeleteMealEventListener(mealView));

      TabSheet tabs = new TabSheet();
      tabs.setSizeFull();

      tabs.addTab(pupilsView, "Bendras sąrašas", FontAwesome.USERS);
      tabs.addTab(mealView, "Maitinimosi žurnalas", FontAwesome.COFFEE);
      return tabs;
   }

   private boolean valid(Long id, Date date) {
      if (id == null) {
         Notification.show("Nepasirinktas mokinys", WARNING_MESSAGE);
         return false;
      } else if (date == null) {
         Notification.show("Nenurodyta data", WARNING_MESSAGE);
         return false;
      } else if (pupilService.reachedMealLimit(id, date)) {
         Notification.show("Viršijamas nurodytos dienos maitinimosi limitas", WARNING_MESSAGE);
         return false;
      } else {
         return true;
      }
   }

   private ItemClickEvent.ItemClickListener newPulilListTableClickListener() {
      return event -> {
         Item item = event.getItem();
         long id = (long) event.getItemId();
         if (event.isDoubleClick()) {
            PupilEditWindow editView = new PupilEditWindow(item, getImage(id), mealService.lastMealEvent(id));
            editView.addCloseButtonListener(closeBtnClick -> editView.close());
            editView.addSaveButtonListener(
                  saveBtnClick -> {
                     editView.commit();
                     boolean dinnerPermission = (boolean) item.getItemProperty("dinnerPermitted").getValue();
                     boolean breakfastPermission = (boolean) item.getItemProperty("breakfastPermitted").getValue();
                     String comment = (String) item.getItemProperty("comment").getValue();
                     pupilService.saveOrUpdate(new PupilInfo(id, breakfastPermission, dinnerPermission, comment));
                     editView.close();
                     Notification.show("Išsaugota", TRAY_NOTIFICATION);
                  });
            UI.getCurrent().addWindow(editView);
         }
      };
   }

   private Image getImage(long id) {
      String remoteImgUrl = adbImageBasePath + id + imageExtension;
      Resource resource;
      if (remoteImageExists(remoteImgUrl)) {
         resource = new ExternalResource(remoteImgUrl);
      } else {
         String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
         resource = new FileResource(new File(basePath + File.separator + noPhotoPath));
      }
      return new Image("", resource);
   }

   private boolean remoteImageExists(String url) {
      try {
         URL u = new URL(url);
         HttpURLConnection http = (HttpURLConnection) u.openConnection();
         http.setInstanceFollowRedirects(false);
         http.setRequestMethod("HEAD");
         http.connect();
         return (http.getResponseCode() == HttpURLConnection.HTTP_OK);
      } catch (Exception e) {
         return false;
      }
   }

   private ClickListener newAddMealEventListener(MealEventListView mealView) {
      return click -> {
         MealEventManualCreateWindow w = new MealEventManualCreateWindow();
         w.getContainer().addAll(pupilService.getMergedMealAllowedList());
         w.addCloseButtonListener(closeClick -> w.close());
         w.addSaveButtonListener(saveClick -> {
            if (valid((Long) w.getTable().getValue(), w.getDateField().getValue())) {
               AdbPupilDto dto = pupilService.getByCardId((long) w.getTable().getValue());
               String name = dto.getFirstName() + " " + dto.getLastName();
               MealEvent m = new MealEvent(dto.getCardId(), name, w.getDateField().getValue());
               mealService.saveMealEvent(m);
               mealView.getContainer().addBean(m);
               w.close();
            }
         });
         UI.getCurrent().addWindow(w);
      };
   }

   private ClickListener newDeleteMealEventListener(MealEventListView mealView) {
      return click -> {
         Long id = (Long) mealView.getTable().getValue();
         if (id == null) {
            Notification.show("Niekas nepasirinkta", WARNING_MESSAGE);
         } else {
            mealService.deleteMealEvent(id);
            mealView.getContainer().removeItem(id);
            mealView.getTable().select(null);
            Notification.show("Įrašas pašalintas", TRAY_NOTIFICATION);
         }
      };
   }
}
