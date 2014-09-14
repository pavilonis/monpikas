package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.dao.PupilDto;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.TablePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;

@Controller
public class ViewController {

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MessageSource messageSource;

   public TabSheet createAdbPulilListView() {
      TabSheet tabs = new TabSheet();
      tabs.setSizeFull();
      TablePanel tablePanel = new TablePanel();

      List<PupilDto> originPupils = pupilService.getOriginalList();
      tablePanel.getContainer().addAll(originPupils);
      tablePanel.setTableClickListener(newClickListener());

      tabs.addTab(tablePanel, "Bendras sarašas");

      return tabs;
   }

   private ItemClickEvent.ItemClickListener newClickListener() {
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
                     (boolean) event.getItem().getItemProperty("dinner").getValue(),
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
