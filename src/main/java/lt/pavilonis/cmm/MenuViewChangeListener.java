package lt.pavilonis.cmm;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.StreamSupport;

public class MenuViewChangeListener implements ViewChangeListener {

   private static final String STYLE_SELECTED = "selected";
   private final CssLayout menuLayout;
   private final Map<String, String> menuItems;
   private final CssLayout menuItemsLayout;

   public MenuViewChangeListener(CssLayout menuLayout, Map<String, String> menuItems, CssLayout menuItemsLayout) {
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
      menuItemsLayout.forEach(item -> item.removeStyleName(STYLE_SELECTED));
      menuItems.entrySet().stream()
            .filter(item -> event.getViewName().equals(item.getKey()))
            .map(Map.Entry::getValue)
            .findAny()
            .ifPresent(value -> StreamSupport.stream(menuItemsLayout.spliterator(), false)
                  .filter(c -> StringUtils.startsWith(c.getCaption(), value))
                  .findAny()
                  .ifPresent(c -> c.setStyleName(STYLE_SELECTED)));

      menuLayout.removeStyleName("valo-menuLayout-visible");
   }

}

