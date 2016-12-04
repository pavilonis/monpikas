package lt.pavilonis.cmm.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

   @Autowired
   private UserRestRepository repo;

   @Autowired
   private UserTable table;

   @Autowired
   private ControlPanel controlPanel;

   @Override
   protected void init(VaadinRequest request) {
      addStyles();

      setContent(new MVerticalLayout(controlPanel, table).withFullWidth().expand(table));
   }

   private void addStyles() {
      Page.Styles styles = Page.getCurrent().getStyles();
      styles.add(
            ".valo.v-app, .valo.v-app-loading { " +
                  "  font-family: sans-serif; " +
                  "  font-weight: 500 " +
                  "} " +

                  ".valo .v-margin-top { " +
                  "  padding-top: 20px " +
                  "} " +

                  ".valo .v-margin-right { " +
                  "  padding-right: 20px " +
                  "} " +

                  ".valo .v-margin-bottom { " +
                  "  padding-bottom: 20px " +
                  "} " +

                  ".valo .v-margin-left { " +
                  "  padding-left: 20px " +
                  "} " +
                  ".redicon .v-icon { " +
                  "     color: red; " +
                  "} " +
                  ".gwt-FileUpload { display: none } " +
                  ".user-photo { max-width: 230px; max-height: 300px; } " +

                  ".time-only .v-inline-datefield-calendarpanel-header," +
                  ".time-only .v-inline-datefield-calendarpanel-body {" +
                  "  display: none;" +
                  "}" +
                  ".table-border-less > div {" +
                  "  border: 0 !important; " +
                  "}"
      );
   }
}
