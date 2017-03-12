package lt.pavilonis.cmm.common;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractViewController {

   @Autowired
   protected MessageSourceAdapter messageSource;

   public Component getView() {

      AbstractOrderedLayout layout = getRootLayout();

      Component header = getHeader();
      if (header != null) {
         layout.addComponent(header);
      }

      Component mainArea = getMainArea();
      layout.addComponent(mainArea);

      Component footer = getFooter();
      if (footer != null) {
         layout.addComponent(footer);
      }

      layout.setExpandRatio(mainArea, 1f);
      return layout;
   }

   protected Component getFooter() {
      return null;
   }

   protected Component getHeader() {
      return null;
   }

   protected abstract Component getMainArea();

   protected AbstractOrderedLayout getRootLayout() {
      VerticalLayout layout = new VerticalLayout();
      layout.setSizeFull();
      return layout;
   }
}
