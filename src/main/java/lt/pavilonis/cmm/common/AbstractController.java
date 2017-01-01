package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import lt.pavilonis.cmm.config.App;
import lt.pavilonis.cmm.MessageSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

public abstract class AbstractController implements ListController {

   @Autowired
   private MessageSourceAdapter messages;

   @Override
   public Button getMenuButton() {
      return new MButton()
            .withWidth("170px")
            .withIcon(getMenuIcon())
            .withCaption(getMenuButtonCaption())
            .withStyleName("text-align-left");
   }

   @Override
   public Layout getListLayout() {

      Component filterPanel = App.context.getBean(getFilterPanelClass());
      Component table = App.context.getBean(getTableClass());

      return new MVerticalLayout(filterPanel, table)
            .expand(table);
   }

   @Override
   public String getMenuButtonCaption() {
      return messages.get(this, "caption");
   }

   protected abstract Class<? extends Component> getFilterPanelClass();

   protected abstract Class<? extends Component> getTableClass();

   protected abstract FontAwesome getMenuIcon();
}
