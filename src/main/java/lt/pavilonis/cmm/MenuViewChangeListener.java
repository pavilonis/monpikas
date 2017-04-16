package lt.pavilonis.cmm;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.StreamSupport;

public class MenuViewChangeListener implements ViewChangeListener {

   private static final String STYLE_SELECTED = "selected";
   private final CssLayout menuLayout;
   private final List<MenuLinkGroup> menuLinkGroups;
   private final CssLayout menuItemsLayout;

   public MenuViewChangeListener(CssLayout menuLayout, List<MenuLinkGroup> menuLinkGroups, CssLayout menuItemsLayout) {
      this.menuLayout = menuLayout;
      this.menuLinkGroups = menuLinkGroups;
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
      menuLinkGroups.stream()
            .flatMap(group -> group.getLinks().stream())
            .filter(link -> link.getCode().equals(newPageCode))
            .findAny()
            .ifPresent(
                  link -> StreamSupport.stream(menuItemsLayout.spliterator(), false)
                        .filter(c -> StringUtils.startsWith(c.getCaption(), App.translate("Menu", link.getCode())))
                        .findAny()
                        .ifPresent(c -> c.setStyleName(STYLE_SELECTED))
            );

      menuLayout.removeStyleName("valo-menuLayout-visible");
   }
}

