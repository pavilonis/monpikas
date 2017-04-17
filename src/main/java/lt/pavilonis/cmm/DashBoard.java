package lt.pavilonis.cmm;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = DashBoard.VIEW_NAME)
public class DashBoard extends VerticalLayout implements View {

   static final String VIEW_NAME = "dashboard";

   @Autowired
   public DashBoard() {
      addComponent(new Label("ČMM"));
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
   }
}
