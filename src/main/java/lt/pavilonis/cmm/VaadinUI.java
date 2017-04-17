package lt.pavilonis.cmm;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.common.MenuItemViewProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringUI
@Theme("custom")
public class VaadinUI extends UI {
   //
   @Autowired
   private List<MenuItemViewProvider> viewProviders;

   @Override
   protected void init(VaadinRequest request) {

      RootLayout rootLayout = new RootLayout();

      Navigator navigator = new Navigator(this, rootLayout.getContentContainer());

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

      return viewProviders.stream()
            .filter(provider -> userRoles.contains(provider.getViewRole()))
            .collect(Collectors.groupingBy(
                  MenuItemViewProvider::getViewGroupName,
                  Collectors.mapping(
                        provider -> new MenuItem(provider.getViewName(), provider.getViewIcon()),
                        Collectors.toList()
                  )
            ));
   }

   private Set<String> currentUserRoles() {
      SecurityContext context = SecurityContextHolder.getContext();
      Authentication authentication = context.getAuthentication();
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
   }
}
