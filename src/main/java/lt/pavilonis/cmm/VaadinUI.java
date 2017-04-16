package lt.pavilonis.cmm;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;

@SpringUI
@Theme("custom")
public class VaadinUI extends UI {

   @Override
   protected void init(VaadinRequest request) {

      RootLayout rootLayout = new RootLayout();
      Navigator navigator = new Navigator(this, rootLayout.getContentContainer());

      CssLayout menuLayout = new MenuLayout(navigator);
      rootLayout.addMenu(menuLayout);
      setContent(rootLayout);

      String fragment = Page.getCurrent().getUriFragment();
      if (StringUtils.isBlank(fragment)) {
         navigator.navigateTo("dashboard");
      }
      getPage().setTitle("CMM");
   }
}
