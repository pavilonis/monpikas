package lt.pavilonis.cmm;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.common.MenuItemViewProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lt.pavilonis.cmm.App.translate;

@SpringUI
@Theme("custom")
public class VaadinUI extends UI {

   private final List<MenuItemViewProvider> viewProviders;
   private final RootLayout rootLayout;

   public VaadinUI(List<MenuItemViewProvider> viewProviders, RootLayout rootLayout) {
      this.viewProviders = viewProviders;
      this.rootLayout = rootLayout;
   }

   @Override
   protected void init(VaadinRequest request) {

      Navigator navigator = getNavigator();
      viewProviders.forEach(navigator::addProvider);

      Map<String, List<MenuItem>> menuStructure = createMenuStructure();
      CssLayout menuLayout = new MenuLayout(navigator, menuStructure);
      rootLayout.addMenu(menuLayout);
      setContent(rootLayout);

      String fragment = Page.getCurrent().getUriFragment();
      if (StringUtils.isBlank(fragment)) {
         navigator.navigateTo("dashboard");
      }
      getPage().setTitle("CMM");
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
      return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(toSet());
   }
}
