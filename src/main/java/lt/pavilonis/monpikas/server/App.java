package lt.pavilonis.monpikas.server;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.monpikas.server.controllers.ViewController;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Scope("prototype")
@Theme("valo")
@PreserveOnRefresh
public class App extends UI {

   private static final Logger LOG = getLogger(App.class.getSimpleName());

   private VaadinRequest request;

   @Autowired
   private ViewController controller;

   @Override
   protected void init(final VaadinRequest request) {

      this.request = request;
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
                  "} "+
                  ".redicon .v-icon { " +
                  "     color: red; " +
                  "} "
      );
      VaadinSession.getCurrent().setErrorHandler(event -> {
         LOG.error("ErrorCause -> " + event.getThrowable().getCause());
         LOG.error("ErrorMsg -> " + event.getThrowable().getMessage());
         LOG.error("ErrorStackTrace -> ");
         event.getThrowable().printStackTrace();
         Notification.show("Klaida :'(\n ", event.getThrowable().toString(), Notification.Type.ERROR_MESSAGE);
      });
      VerticalLayout vl = new VerticalLayout();
      vl.setSizeFull();
      vl.setSpacing(true);
      controller.attachComponents(vl);
      setContent(vl);
      setSizeFull();
   }

   public VaadinRequest getRequest() {
      return request;
   }
   //   @Override
//   public void close() {
//
//   }
}
