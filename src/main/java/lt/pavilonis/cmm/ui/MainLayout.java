package lt.pavilonis.cmm.ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import lt.pavilonis.cmm.common.ListController;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

@SpringComponent
@UIScope
public class MainLayout extends MHorizontalLayout {

   private static final MLabel APP_LABEL = new MLabel("<h2>V.O.B.L.A.</h2><h3>Vidinė Organizacijos Bendra Laiko Apskaita<h3>")
         .withSize(MSize.size("500px", "200px"))
         .withContentMode(ContentMode.HTML);

   private Component currentComponent = APP_LABEL;
   private MVerticalLayout stage = new MVerticalLayout(currentComponent)
         .withSize(MSize.FULL_SIZE)
         .alignAll(Alignment.MIDDLE_CENTER);

   @Autowired
   public MainLayout(List<ListController> listControllers) {
      setSizeFull();
      MVerticalLayout menuBar = new MVerticalLayout()
            .withWidth("210px")
            .alignAll(Alignment.TOP_CENTER);


      add(menuBar, stage).expand(stage);

      listControllers
            .stream()
            .sorted((i1, i2) -> i1.getMenuButtonCaption().compareTo(i2.getMenuButtonCaption()))
            .forEach(controller -> {
               Button menuButton = controller.getMenuButton();
               menuButton.addClickListener(click -> updateStage(controller));
               menuBar.add(menuButton);
            });
   }

   private void updateStage(ListController controller) {
      Layout newComponent = controller.getListLayout();
      stage.removeComponent(currentComponent);
      stage.addComponent(newComponent);
      currentComponent = newComponent;
   }
}
