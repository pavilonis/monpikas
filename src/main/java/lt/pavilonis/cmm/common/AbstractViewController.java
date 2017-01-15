package lt.pavilonis.cmm.common;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Optional;

public abstract class AbstractViewController {

   @Autowired
   protected MessageSourceAdapter messages;

   public Component getView() {
      AbstractOrderedLayout layout = getRootLayout();

      getHeader()
            .ifPresent(layout::addComponent);

      Component mainArea = getMainArea();

      layout.addComponent(mainArea);
      layout.setExpandRatio(mainArea, 1f);

      return layout;
   }

   protected Optional<Component> getHeader() {
      return Optional.empty();
   }

   protected abstract Component getMainArea();

   protected AbstractOrderedLayout getRootLayout() {
      return new MVerticalLayout();
   }
}
