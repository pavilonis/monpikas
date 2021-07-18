package lt.pavilonis.cmm.dashboard;

import com.vaadin.data.ValueProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ListGrid;

import java.util.List;
import java.util.Map;

@SpringView(name = Dashboard.VIEW_NAME)
public class Dashboard extends GridLayout implements View {

   static final String VIEW_NAME = "dashboard";

   private final DashboardRepository repository;

   public Dashboard(DashboardRepository repository) {
      super(2, 2);
      this.repository = repository;
      addComponent(scannerEventsComponent());
      setSizeFull();
   }

   private Component scannerEventsComponent() {
      List<ScannerEvents> events = repository.scannerEvents();
      var grid = new ListGrid<>(ScannerEvents.class) {
         @Override
         protected List<String> columnOrder() {
            return List.of("scannerName", "scansToday", "scansTotal");
         }

         @Override
         protected Map<String, ValueProvider<ScannerEvents, ?>> getCustomColumns() {
            return Map.of("scannerName", ScannerEvents::getScannerName);
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
