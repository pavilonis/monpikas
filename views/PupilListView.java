package lt.pavilonis.monpikas.server.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class PupilListView extends Panel implements View {

   private TabSheet tabSheet = new TabSheet();

   public PupilListView() {
      setSizeFull();

      VerticalLayout layout = new VerticalLayout();
      layout.setSpacing(true);
      layout.setMargin(true);
      tabSheet.setSizeFull();


//      container = new BeanContainer<>(PupilDto.class);
//      container.setBeanIdProperty("cardId");
//      container.addAll(pupilService.getOriginPupils());

      layout.addComponent(tabSheet);
      setContent(layout);
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
   }

   public TabSheet getTabSheet() {
      return tabSheet;
   }
}
