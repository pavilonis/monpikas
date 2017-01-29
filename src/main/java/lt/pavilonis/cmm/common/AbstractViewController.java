package lt.pavilonis.cmm.common;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;

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
      return new MVerticalLayout()
            .withSize(MSize.FULL_SIZE)
            .withMargin(false);
   }
}
