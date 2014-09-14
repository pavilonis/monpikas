package lt.pavilonis.monpikas.server.controllers;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.dao.PupilDto;
import lt.pavilonis.monpikas.server.service.PupilService;
import lt.pavilonis.monpikas.server.views.PupilEditWindow;
import lt.pavilonis.monpikas.server.views.PupilListView;
import lt.pavilonis.monpikas.server.views.TablePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;

@Controller
public class ViewController {

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MessageSource messageSource;

   public Panel createAdbPulilListView() {
      PupilListView view = new PupilListView();
      TablePanel tablePanel = new TablePanel();

      List<PupilDto> originPupils = pupilService.getOriginPupils();
      tablePanel.getContainer().addAll(originPupils);
      tablePanel.setTableClickListener(newClickListener());

      view.getTabSheet().addTab(tablePanel, "Bendras sarašas");

      return view;
   }

   private ItemClickEvent.ItemClickListener newClickListener() {
      return event -> {
         if (event.isDoubleClick()) {
            PupilDto pupil = pupilService.getPupil((long) event.getItemId());
            UI.getCurrent().addWindow(
                  new PupilEditWindow(
                        pupil,
                        messageSource.getMessage(
                              "PupilEditWindow.Caption",
                              new Object[]{pupil.getFirstName(), pupil.getLastName()},
                              Locale.getDefault()
                        )
                  )
            );
         }
      };
   }
}
