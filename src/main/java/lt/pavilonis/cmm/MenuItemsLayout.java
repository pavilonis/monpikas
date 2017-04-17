package lt.pavilonis.cmm;

import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Map;

public class MenuItemsLayout extends CssLayout {

   public MenuItemsLayout(Map<String, List<MenuItem>> menuStructure, Navigator navigator) {
      setPrimaryStyleName("valo-menuitems");

      menuStructure.forEach((groupCodeName, groupedMenuItems) -> {

         Label groupLabel = createGroupLabel(App.translate("LinkGroup", groupCodeName));
         addComponent(groupLabel);

         for (MenuItem item : groupedMenuItems) {

            addComponent(createButton(item, navigator));
         }
      });
   }

   private Label createGroupLabel(String text) {
      Label label;
      label = new Label(text, ContentMode.HTML);
      label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
      label.addStyleName(ValoTheme.LABEL_H4);
      label.setSizeUndefined();
      return label;
   }

   private Button createButton(MenuItem item, Navigator navigator) {
      Button button = new Button(createButtonCaption(item.getCodeName()), item.getIcon());
      button.addClickListener(
            event -> navigator.navigateTo(item.getCodeName())
      );
      button.setCaptionAsHtml(true);
      button.setPrimaryStyleName(ValoTheme.MENU_ITEM);
      return button;
   }


   private String createButtonCaption(String captionCode) {
      String translatedCaption = App.translate("Menu", captionCode);

      if (RandomUtils.nextInt(1, 5) != 1) {
         return translatedCaption;
      }
      return translatedCaption + " <span class=\"valo-menu-badge\">12</span>";
   }
}
