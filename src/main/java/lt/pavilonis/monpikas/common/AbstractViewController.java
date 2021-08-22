package lt.pavilonis.monpikas.common;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractViewController implements MenuItemViewProvider {

   public ViewLayout getView() {

      ViewLayout layout = getRootLayout();

      Component header = getHeader();
      if (header != null) {
         layout.addComponent(header);
      }

      Component mainArea = getMainArea();
      layout.addComponent(mainArea);

      getFooter(mainArea).ifPresent(layout::addComponent);

      layout.setExpandRatio(mainArea, 1f);
      return layout;
   }

   protected Optional<Component> getFooter(Component mainArea) {
      return Optional.empty();
   }

   protected Component getHeader() {
      return null;
   }

   protected abstract Component getMainArea();

   protected ViewLayout getRootLayout() {
      return new ViewLayout();
   }

   @Override
   public View getView(String viewName) {
      return getView();
   }

   @Override
   public String getViewName(String viewAndParameters) {
      return Objects.equals(viewAndParameters, getViewName())
            ? viewAndParameters
            : null;
   }
}
