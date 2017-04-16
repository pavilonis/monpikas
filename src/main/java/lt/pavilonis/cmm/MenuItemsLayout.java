package lt.pavilonis.cmm;

import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class MenuItemsLayout extends CssLayout {

   public MenuItemsLayout(List<MenuLinkGroup> linkGroups, Navigator navigator) {
      setPrimaryStyleName("valo-menuitems");

      for (MenuLinkGroup linkGroup : linkGroups) {

         Label groupLabel = createGroupLabel(App.translate("LinkGroup", linkGroup.getCodeName()));
         addComponent(groupLabel);

         for (MenuLink link : linkGroup.getLinks()) {
            Button linkButton = createButton(link, navigator);
            addComponent(linkButton);
            navigator.addView(link.getCode(), link.getViewClass());
         }
      }
   }


   private Label createGroupLabel(String text) {
      Label label;
      label = new Label(text, ContentMode.HTML);
      label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
      label.addStyleName(ValoTheme.LABEL_H4);
      label.setSizeUndefined();
      return label;
   }

   private Button createButton(MenuLink link, Navigator navigator) {
      String caption = App.translate("Menu", link.getCode());
      Button button = new Button(createButtonCaption(caption), link.getIcon());
      button.addClickListener(event -> navigator.navigateTo(link.getCode()));
      button.setCaptionAsHtml(true);
      button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
      return button;
   }


   private String createButtonCaption(String caption) {
      if (RandomUtils.nextInt(1, 5) == 1) {
         return caption + " <span class=\"valo-menu-badge\">12</span>";
      }
      return caption;
   }
}
