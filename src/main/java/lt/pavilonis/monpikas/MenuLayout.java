package lt.pavilonis.monpikas;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.boot.info.BuildProperties;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.stream.Collectors.toList;

public class MenuLayout extends CssLayout {

   public MenuLayout(Navigator navigator, Map<String, List<MenuItem>> menuStructure, BuildProperties buildProperties) {

      var menuItemsLayout = new MenuItemsLayout(menuStructure, navigator);
      addComponents(createMenuHeader(buildProperties), createUserSettings(), menuItemsLayout);

      List<MenuItem> menuItems = menuStructure.values().stream()
            .flatMap(Collection::stream)
            .collect(toList());

      navigator.addViewChangeListener(new MenuViewChangeListener(this, menuItems, menuItemsLayout));
   }

   private MenuBar createUserSettings() {
      var settings = new MenuBar();
      settings.addStyleName("user-menu");

      var icon = new ThemeResource("profile-pic-300px.jpg");
      MenuBar.MenuItem settingsItem = settings.addItem("Vardas Pavardė", icon, null);
      settingsItem.addItem("TODO1", null);
      settingsItem.addSeparator();
      settingsItem.addItem("TODO2", null);
      return settings;
   }

   private HorizontalLayout createMenuHeader(BuildProperties buildProperties) {
      var title = new Label("" +
            "<h3><strong>Monpikas</strong> " +
            "  <span style='color:darkgrey'>" +
            "     " + "v" + buildProperties.getVersion() +
            "  </span>" +
            "</h3>", ContentMode.HTML
      );
      title.setSizeUndefined();
      var layout = new HorizontalLayout(title);
      layout.setExpandRatio(title, 1);
      layout.setWidth("100%");
      layout.setSpacing(false);
      layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
      layout.addStyleName(ValoTheme.MENU_TITLE);
      return layout;
   }
}
