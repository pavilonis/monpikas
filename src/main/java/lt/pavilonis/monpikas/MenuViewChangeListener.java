package lt.pavilonis.monpikas;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class MenuViewChangeListener implements ViewChangeListener {

   private static final String STYLE_SELECTED = "selected";
   private final CssLayout menuLayout;
   private final List<MenuItem> menuItems;
   private final CssLayout menuItemsLayout;

   public MenuViewChangeListener(CssLayout menuLayout, List<MenuItem> menuItems,
                                 CssLayout menuItemsLayout) {
      this.menuLayout = menuLayout;
      this.menuItems = menuItems;
      this.menuItemsLayout = menuItemsLayout;
   }

   @Override
   public boolean beforeViewChange(ViewChangeEvent event) {
      return true;
   }

   @Override
   public void afterViewChange(ViewChangeEvent event) {
      String newPageCode = event.getViewName();
      menuItemsLayout.forEach(item -> item.removeStyleName(STYLE_SELECTED));

      menuItems.stream()
            .filter(item -> item.getCodeName().equals(newPageCode))
            .findAny()
            .ifPresent(item -> {
               String caption = App.translate(item, item.getCodeName());
               StreamSupport.stream(menuItemsLayout.spliterator(), false)
                     .filter(c -> Objects.equals(c.getCaption(), caption))
                     .findAny()
                     .ifPresent(c -> c.setStyleName(STYLE_SELECTED));
            });

      menuLayout.removeStyleName("valo-menuLayout-visible");
   }
}

