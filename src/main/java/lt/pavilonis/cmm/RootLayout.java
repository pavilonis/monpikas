package lt.pavilonis.cmm;


import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RootLayout extends HorizontalLayout {

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
      CssLayout layout = new CssLayout();
      layout.setPrimaryStyleName("valo-content");
      layout.addStyleName("v-scrollable");
      layout.setSizeFull();

      return layout;
   }

   private CssLayout createMenuArea() {
      CssLayout layout = new CssLayout();
      layout.setPrimaryStyleName(ValoTheme.MENU_ROOT);
      return layout;
   }

}
