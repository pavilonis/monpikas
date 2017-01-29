package lt.pavilonis.cmm.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
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
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@SpringComponent
@UIScope
public class MainLayout extends MHorizontalLayout {

   private final Map<Class<? extends AbstractViewController>, Component> scopeComponents = new HashMap<>();

   private final MLabel appLabel = new MLabel("<h2>ČMM</h2><h3><h3>")
         .withSize(MSize.size("500px", "200px"))
         .withContentMode(ContentMode.HTML);

   private Component currentComponent = appLabel;
   private MVerticalLayout stage = new MVerticalLayout(currentComponent)
         .withSize(MSize.FULL_SIZE)
         .alignAll(Alignment.MIDDLE_CENTER);

   @Autowired
   public MainLayout(MessageSourceAdapter messages) {
      setSizeFull();
      MVerticalLayout menuBar = new MVerticalLayout()
            .withWidth("210px")
            .alignAll(Alignment.TOP_CENTER);


      add(menuBar, stage).expand(stage);

      Stream.of(
            new MenuButton(CanteenReportViewController.class, FontAwesome.FILE_EXCEL_O),
            new MenuButton(MealListController.class, FontAwesome.WRENCH),
            new MenuButton(MealEventListController.class, FontAwesome.CUTLERY),
            new MenuButton(UserMealListController.class, FontAwesome.CHILD),
            new MenuButton(UserListController.class, FontAwesome.USER),
            new MenuButton(KeyListController.class, FontAwesome.KEY),
            new MenuButton(UserRolesListController.class, FontAwesome.SITEMAP)
      ).forEach(
            button -> {
               // TODO add role check here
               Class<? extends AbstractViewController> clazz = button.getControllerClass();

               button.setCaption(messages.get(clazz, "caption"));
               button.addClickListener(click -> updateStage(clazz));

               menuBar.add(button);
            }
      );
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
