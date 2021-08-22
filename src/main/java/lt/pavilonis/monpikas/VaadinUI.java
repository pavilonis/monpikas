package lt.pavilonis.monpikas;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lt.pavilonis.monpikas.common.MenuItemViewProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lt.pavilonis.monpikas.App.translate;

@SpringUI
@Theme("custom")
public class VaadinUI extends UI {

   private static final Logger LOGGER = LoggerFactory.getLogger(VaadinUI.class);
   private final List<MenuItemViewProvider> viewProviders;
   private final RootLayout rootLayout;
   private final BuildProperties buildProperties;

   public VaadinUI(List<MenuItemViewProvider> viewProviders, RootLayout rootLayout, BuildProperties buildProperties) {
      this.viewProviders = viewProviders;
      this.rootLayout = rootLayout;
      this.buildProperties = buildProperties;
   }

   @Override
   protected void init(VaadinRequest request) {

      Navigator navigator = getNavigator();
      viewProviders.forEach(navigator::addProvider);

      Map<String, List<MenuItem>> menuStructure = createMenuStructure();
      rootLayout.addMenu(new MenuLayout(navigator, menuStructure, buildProperties));
      setContent(rootLayout);

      String fragment = Page.getCurrent().getUriFragment();
      if (!StringUtils.hasText(fragment)) {
         navigator.navigateTo("dashboard");
      }
      getPage().setTitle("Monpikas");
   }

   private Map<String, List<MenuItem>> createMenuStructure() {
      Set<String> userRoles = currentUserRoles();

      Map<String, List<MenuItem>> result = viewProviders.stream()
            .filter(provider -> userRoles.contains(provider.getViewRole()))
            .collect(groupingBy(
                  MenuItemViewProvider::getViewGroupName,
                  () -> new TreeMap<>(comparing(a -> translate("MenuItemGroup", a))),
                  mapping(provider -> new MenuItem(provider.getViewName(), provider.getViewIcon()), toList())
            ));

      result.put("other", List.of(new MenuItem("dashboard", VaadinIcons.LINE_CHART)));
      return result;
   }

   private Set<String> currentUserRoles() {
      Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

      if (authentication == null) {
         LOGGER.error("Could not get context");
         return Set.of();
      }

      return authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(toSet());
   }
}
