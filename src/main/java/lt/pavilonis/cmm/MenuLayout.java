package lt.pavilonis.cmm;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Arrays;
import java.util.List;

public class MenuLayout extends CssLayout {

   public MenuLayout(Navigator navigator) {

      List<MenuLinkGroup> linkGroups = createLinkGroups();

      CssLayout menuItemsLayout = new MenuItemsLayout(linkGroups, navigator);

      addComponents(
            createMenuHeader(),
            createUserSettings(),
            menuItemsLayout
      );

      MenuViewChangeListener viewChangeListener = new MenuViewChangeListener(
            this,
            linkGroups,
            menuItemsLayout
      );
      navigator.addViewChangeListener(viewChangeListener);
   }

   protected List<MenuLinkGroup> createLinkGroups() {
      return Arrays.asList(
            new MenuLinkGroup("groupOne", "ROLE_GROUP1", Arrays.asList(
                  new MenuLink("dashboard", CommonParts.class, VaadinIcons.CLOUD),
                  new MenuLink("labels", Labels.class, VaadinIcons.CLOUD_DOWNLOAD),
                  new MenuLink("menubars", MenuBars.class, VaadinIcons.CLOUD_UPLOAD),
                  new MenuLink("calendar", Labels.class, VaadinIcons.EDIT)
            )),
            new MenuLinkGroup("groupTwo", "ROLE_GROUP2", Arrays.asList(
                  new MenuLink("dashboard2", CommonParts.class, VaadinIcons.CLOUD),
                  new MenuLink("labels2", Labels.class, VaadinIcons.CLOUD_DOWNLOAD),
                  new MenuLink("menubars2", MenuBars.class, VaadinIcons.CLOUD_UPLOAD),
                  new MenuLink("calendar2", Labels.class, VaadinIcons.EDIT)
            ))
      );
   }

   private MenuBar createUserSettings() {
      MenuBar settings = new MenuBar();
      settings.addStyleName("user-menu");

      ThemeResource icon = new ThemeResource("profile-pic-300px.jpg");
      MenuBar.MenuItem settingsItem = settings.addItem("Vardas Pavardė", icon, null);
      settingsItem.addItem("Preferences", null);
      settingsItem.addSeparator();
      settingsItem.addItem("Sign Out", null);
      return settings;
   }

   private HorizontalLayout createMenuHeader() {
      Label title = new Label(
            //TODO update version with maven
            "<h3>ČMM <strong>Monpikas</strong> <span style='color:darkgrey'>v0.91</span></h3>", ContentMode.HTML
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
