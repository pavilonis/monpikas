package lt.pavilonis.monpikas.server;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
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
@Theme("valo")
public class App extends UI {

   @Autowired
   private ViewController controller;

   @Override
   protected void init(final VaadinRequest request) {
      Page.Styles styles = Page.getCurrent().getStyles();
      styles.add(
            ".valo.v-app, .valo.v-app-loading { " +
                  "     font-family: sans-serif; " +
                  "     font-weight: 500 " +
                  "} " +

                  ".valo .v-margin-top { " +
                  "     padding-top: 20px " +
                  "} " +

                  ".valo .v-margin-right { " +
                  "     padding-right: 20px " +
                  "} " +

                  ".valo .v-margin-bottom { " +
                  "     padding-bottom: 20px " +
                  "} " +

                  ".valo .v-margin-left { " +
                  "     padding-left: 20px " +
                  "} ");
      VaadinSession.getCurrent().setErrorHandler(event -> {
         System.out.println("ErrorCause -> " + event.getThrowable().getCause());
         System.out.println("ErrorMsg -> " + event.getThrowable().getMessage());
         System.out.print("ErrorStackTrace -> ");
         event.getThrowable().printStackTrace();
         Notification.show("Klaida :'(\n ", event.getThrowable().toString(), Notification.Type.ERROR_MESSAGE);
      });
      setContent(controller.createAdbPulilListView());
      setSizeFull();
   }
}
