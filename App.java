package lt.pavilonis.monpikas.server;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.server.controllers.ViewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class App extends UI {

   @Autowired
   private ViewController controller;

   @Override
   protected void init(final VaadinRequest request) {

      VaadinSession.getCurrent().setErrorHandler(event -> {
         System.out.println("ErrorCause -> "+event.getThrowable().getCause());
         System.out.println("ErrorMsg -> " + event.getThrowable().getMessage());
         System.out.print("ErrorStackTrace -> ");
         event.getThrowable().printStackTrace();
         Notification.show("Klaida :'(\n ", event.getThrowable().getCause().toString(), Notification.Type.ERROR_MESSAGE);
      });
      setContent(controller.createAdbPulilListView());
      setSizeFull();
   }
}
