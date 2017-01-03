package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

public abstract class AbstractViewController implements ListController {

   @Autowired
   private MessageSourceAdapter messages;

   @Override
   public Button getMenuButton() {
      return new MButton()
            .withWidth("200px")
            .withIcon(getMenuIcon())
            .withCaption(getMenuButtonCaption())
            .withStyleName("text-align-left");
   }

   @Override
   public Layout getListLayout() {
      MVerticalLayout layout = new MVerticalLayout()
            .withMargin(false);

      if (getFilterPanelClass() != null) {
         layout.add(App.context.getBean(getFilterPanelClass()));
      }

      Component main = App.context.getBean(getMainLayoutClass());

      return layout
            .add(main)
            .expand(main);
   }

   @Override
   public String getMenuButtonCaption() {
      return messages.get(this, "caption");
   }

   protected abstract Class<? extends Component> getFilterPanelClass();

   protected abstract Class<? extends Component> getMainLayoutClass();

   protected abstract FontAwesome getMenuIcon();
}