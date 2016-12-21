package lt.pavilonis.cmm.ui;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import lt.pavilonis.cmm.ui.menu.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

@SpringComponent
@UIScope
public class MainLayout extends MHorizontalLayout {

   @Autowired
   public MainLayout(List<MenuItem> items) {
      setSizeFull();
      MVerticalLayout leftMenuBar = new MVerticalLayout()
            .withWidth("220px")
            .alignAll(Alignment.TOP_CENTER);

      items.forEach(leftMenuBar::add);

      MVerticalLayout stage = new MVerticalLayout(
            new MLabel("<h1>V.L.A.D.I.K.</h1>")
                  .withSize(MSize.size("200px", "200px"))
                  .withContentMode(ContentMode.HTML)
      ).withSize(MSize.FULL_SIZE)
            .alignAll(Alignment.MIDDLE_CENTER)
            .withMargin(true);

      stage.setSizeFull();
      items.forEach(item -> item.addClickListener(click -> {
         stage.removeAllComponents();
         item.collectLayoutElements(stage);
      }));

      add(leftMenuBar, stage).expand(stage);
   }
}
