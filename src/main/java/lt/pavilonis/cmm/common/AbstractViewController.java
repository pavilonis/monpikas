package lt.pavilonis.cmm.common;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Optional;

public abstract class AbstractViewController {

   @Autowired
   protected MessageSourceAdapter messages;

   public Component getView() {
      AbstractOrderedLayout layout = getRootLayout();

      Component mainArea = getMainArea();

      getHeader().ifPresent(layout::addComponent);
      layout.addComponent(mainArea);

      layout.setExpandRatio(mainArea, 1f);
      return layout;
   }

   protected Optional<Component> getHeader() {
      return Optional.empty();
   }

   protected abstract Component getMainArea();

   protected AbstractOrderedLayout getRootLayout() {
      return new MVerticalLayout()
            .withSize(MSize.FULL_SIZE)
            .withMargin(false);
   }
}
