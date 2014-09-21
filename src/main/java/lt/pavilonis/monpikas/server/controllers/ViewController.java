package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.service.DinnerService;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.DinnerEventListView;
import lt.pavilonis.monpikas.server.views.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.PupilsListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.util.Locale;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

@Controller
public class ViewController {

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MessageSource messageSource;

   @Autowired
   private DinnerService dinnerService;

   public TabSheet createAdbPulilListView() {
      PupilsListView pupilsView = new PupilsListView();
      pupilsView.getContainer().addAll(pupilService.getOriginalList());
      pupilsView.setTableClickListener(newPulilListTableClickListener());

      DinnerEventListView dinnersView = new DinnerEventListView();
      dinnersView.getContainer().addAll(dinnerService.getDinnerEventList());

      TabSheet tabs = new TabSheet();
      tabs.setSizeFull();
      tabs.addTab(pupilsView, "Bendras sąrašas");
      tabs.addTab(dinnersView, "Pietų žurnalas");
      return tabs;
   }

   private ItemClickEvent.ItemClickListener newPulilListTableClickListener() {
      return event -> {
         if (event.isDoubleClick()) {
            PupilEditWindow editView = new PupilEditWindow(
                  event.getItem(),
                  messageSource.getMessage(
                        "PupilEditWindow.Caption",
                        new Object[]{event.getItem().getItemProperty("firstName"), event.getItem().getItemProperty("lastName")},
                        Locale.getDefault()
                  )
            );
            editView.addCloseButtonListener(closeBtnClick -> editView.close());
            editView.addSaveButtonListener(saveBtnClick -> {
               editView.commit();
               PupilInfo info = new PupilInfo(
                     (long) event.getItemId(),
                     (boolean) event.getItem().getItemProperty("dinnerPermitted").getValue(),
                     (String) event.getItem().getItemProperty("comment").getValue()
               );
               pupilService.saveOrUpdate(info);
               editView.close();
               Notification.show("Išsaugota", TRAY_NOTIFICATION);
            });
            UI.getCurrent().addWindow(editView);
         }
      };
   }
}
