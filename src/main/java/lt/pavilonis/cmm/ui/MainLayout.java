package lt.pavilonis.cmm.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.views.event.MealEventListController;
import lt.pavilonis.cmm.canteen.views.report.CanteenReportViewController;
import lt.pavilonis.cmm.canteen.views.setting.MealListController;
import lt.pavilonis.cmm.canteen.views.user.UserMealListController;
import lt.pavilonis.cmm.common.AbstractViewController;
import lt.pavilonis.cmm.common.MenuButton;
import lt.pavilonis.cmm.ui.key.KeyListController;
import lt.pavilonis.cmm.ui.security.UserRolesListController;
import lt.pavilonis.cmm.ui.user.UserListController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@UIScope
public class MainLayout extends HorizontalLayout {

   private final Map<Class<? extends AbstractViewController>, Component> scopeComponents = new HashMap<>();
   private final Label appLabel = new Label("<h2>ČMM</h2><h3><h3>", ContentMode.HTML);
   private Component currentComponent = appLabel;
   private final VerticalLayout stage = new VerticalLayout(currentComponent);

   @Autowired
   public MainLayout(MessageSourceAdapter messages) {
      setSizeFull();
      stage.setSizeFull();
      stage.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
      appLabel.setWidth(500, Unit.PIXELS);
      appLabel.setHeight(200, Unit.PIXELS);

      VerticalLayout menuBar = new VerticalLayout();
      menuBar.setWidth(210, Unit.PIXELS);
      menuBar.setDefaultComponentAlignment(Alignment.TOP_CENTER);

      addComponents(menuBar, stage);
      setExpandRatio(stage, 1);

      Set<String> userRoles = currentUserRoles();

      Stream.of(
            //TODO move icon and role data somewere
            new MenuButton(CanteenReportViewController.class, "ROLE_MEAL_REPORT", VaadinIcons.EXCHANGE),
            new MenuButton(MealListController.class, "ROLE_MEAL_CONFIG", VaadinIcons.WRENCH),
            new MenuButton(MealEventListController.class, "ROLE_MEAL_EVENTS", VaadinIcons.CUTLERY),
            new MenuButton(UserMealListController.class, "ROLE_USER_MEALS", VaadinIcons.CHILD),
            new MenuButton(UserListController.class, "ROLE_USERS", VaadinIcons.USER),
            new MenuButton(KeyListController.class, "ROLE_KEYS", VaadinIcons.KEY),
            new MenuButton(UserRolesListController.class, "ROLE_ROLES", VaadinIcons.USER_STAR)
      )
            .filter(button -> userRoles.contains(button.getRoleName()))
            .forEach(button -> {
               Class<? extends AbstractViewController> clazz = button.getControllerClass();

               button.setCaption(messages.get(clazz, "caption"));
               button.addClickListener(click -> updateStage(clazz));

               menuBar.addComponent(button);
            });
   }

   private Set<String> currentUserRoles() {
      SecurityContext context = SecurityContextHolder.getContext();
      Authentication authentication = context.getAuthentication();
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
   }

   private void updateStage(Class<? extends AbstractViewController> viewControllerClass) {

      Component newComponent = scopeComponents.get(viewControllerClass);

      if (newComponent == null) {
         AbstractViewController controller = App.context.getBean(viewControllerClass);
         scopeComponents.put(viewControllerClass, newComponent = controller.getView());
      }

      stage.removeComponent(currentComponent);
      stage.addComponent(newComponent);
      currentComponent = newComponent;
   }
}
