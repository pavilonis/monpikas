package lt.pavilonis.monpikas.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewProvider;

public interface MenuItemViewProvider extends ViewProvider {
   VaadinIcons getViewIcon();

   String getViewRole();

   String getViewGroupName();

   String getViewName();
}
