package lt.pavilonis.cmm;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringViewDisplay
public class RootLayout extends HorizontalLayout implements ViewDisplay{

   private final CssLayout contentArea = createContentArea();
   private final CssLayout menuArea = createMenuArea();

   public RootLayout() {
      setSizeFull();
      setSpacing(false);

      addComponents(menuArea, contentArea);
      setExpandRatio(contentArea, 1);
      setWidth("100%");
   }

   public ComponentContainer getContentContainer() {
      return contentArea;
   }

   public void addMenu(Component menu) {
      menu.addStyleName(ValoTheme.MENU_PART);
      menuArea.addComponent(menu);
   }

   private CssLayout createContentArea() {
      var layout = new CssLayout();
      layout.setPrimaryStyleName("valo-content");
      layout.addStyleName("v-scrollable");
      layout.setSizeFull();
      return layout;
   }

   private CssLayout createMenuArea() {
      var layout = new CssLayout();
      layout.setPrimaryStyleName(ValoTheme.MENU_ROOT);
      return layout;
   }

   @Override
   public void showView(View view) {
      contentArea.removeAllComponents();
      contentArea.addComponent((Component)view);
   }
}
