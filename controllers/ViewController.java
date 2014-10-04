package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.MealEventListView;
import lt.pavilonis.monpikas.server.views.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.PupilsListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

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

      MealEventListView dinnersView = new MealEventListView();
      dinnersView.getContainer().addAll(mealService.getDinnerEventList());

      TabSheet tabs = new TabSheet();
      tabs.setSizeFull();

      tabs.addTab(pupilsView, "Bendras sąrašas", FontAwesome.USERS);
      tabs.addTab(dinnersView, "Maitinimosi žurnalas", FontAwesome.COFFEE);
      return tabs;
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
}
