package lt.pavilonis.cmm.security.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class SecurityUserView extends VerticalLayout implements View {

   public SecurityUserView() {
      addComponent(new Label("hello, yopta"));
   }

   @Override
   public void enter(ViewChangeListener.ViewChangeEvent event) {
      System.out.println("hello");
   }
}
