package lt.pavilonis.cmm.dashboard;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.common.service.MessageSourceAdapter;

import java.util.List;
import java.util.Map;

@SpringView(name = Dashboard.VIEW_NAME)
public class Dashboard extends GridLayout implements View {

   static final String VIEW_NAME = "dashboard";

   private final DashboardRepository repository;
   private final MessageSourceAdapter messages;

   public Dashboard(DashboardRepository repository, MessageSourceAdapter messages) {
      super(2, 2);
      this.repository = repository;
      this.messages = messages;
      addComponent(new Label("ČMM"));
      addComponent(scannerEventsComponent());
      setSizeFull();
   }

   private Component scannerEventsComponent() {
      List<ScannerEvents> events = repository.scannerEvents();
      ListGrid<ScannerEvents> grid = new ListGrid<ScannerEvents>(ScannerEvents.class) {
         @Override
         protected List<String> columnOrder() {
            return List.of("scannerName", "scansToday", "scansTotal");
         }

         @Override
         protected Map<String, ValueProvider<ScannerEvents, ?>> getCustomColumns() {
            return Map.of("scannerName", item -> messages.get(Scanner.class, item.getScannerName()));
         }
      };
      grid.setCaption(App.translate(this, "scanStats"));
      grid.setItems(events);
      grid.setSizeFull();
      return grid;
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
   }
}
