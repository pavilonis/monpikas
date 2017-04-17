package lt.pavilonis.cmm;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MenuLayout extends CssLayout {

   private static final String PROPERTY_VERSION = ResourceBundle
         .getBundle("application")
         .getString("application.version");

   public MenuLayout(Navigator navigator, Map<String, List<MenuItem>> menuStructure) {


      CssLayout menuItemsLayout = new MenuItemsLayout(menuStructure, navigator);

      addComponents(
            createMenuHeader(),
            createUserSettings(),
            menuItemsLayout
      );

      List<MenuItem> menuItems = menuStructure.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

      MenuViewChangeListener viewChangeListener =
            new MenuViewChangeListener(this, menuItems, menuItemsLayout);

      navigator.addViewChangeListener(viewChangeListener);
   }

   private MenuBar createUserSettings() {
      MenuBar settings = new MenuBar();
      settings.addStyleName("user-menu");

      ThemeResource icon = new ThemeResource("profile-pic-300px.jpg");
      MenuBar.MenuItem settingsItem = settings.addItem("Vardas Pavardė", icon, null);
      settingsItem.addItem("TODO1", null);
      settingsItem.addSeparator();
      settingsItem.addItem("TODO2", null);
      return settings;
   }

   private HorizontalLayout createMenuHeader() {
      Label title = new Label("" +
            "<h3>ČMM <strong>Monpikas</strong> " +
            "  <span style='color:darkgrey'>" +
            "     " + "v" + PROPERTY_VERSION +
            "  </span>" +
            "</h3>", ContentMode.HTML
      );
      title.setSizeUndefined();
      HorizontalLayout layout = new HorizontalLayout(title);
      layout.setExpandRatio(title, 1);
      layout.setWidth("100%");
      layout.setSpacing(false);
      layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
      layout.addStyleName(ValoTheme.MENU_TITLE);
      return layout;
   }
}
