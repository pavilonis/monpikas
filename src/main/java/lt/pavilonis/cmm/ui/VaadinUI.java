package lt.pavilonis.cmm.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("custom")
public class VaadinUI extends UI {

   @Autowired
   private MainLayout mainLayout;

   @Override
   protected void init(VaadinRequest request) {
      setContent(mainLayout);
   }
}
