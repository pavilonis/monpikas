package lt.pavilonis.cmm.common;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import lt.pavilonis.cmm.App;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MVerticalLayout;

public abstract class AbstractListController extends AbstractViewController {

   @Override
   public Layout getListLayout() {
      MVerticalLayout layout = new MVerticalLayout()
            .withMargin(false);

      maybeAddHeader(layout);

      MTable main = configureTable();

      return layout
            .add(main)
            .expand(main);
   }

   protected MTable configureTable() {
      Class<? extends Component> mainAreaClass = getMainAreaClass();
      if (!mainAreaClass.isAssignableFrom(MTable.class)) {
         throw new IllegalArgumentException(
               "Not suitable main area class. Should extend " + MTable.class.getSimpleName());
      }

      MTable<?> table = (MTable<?>) App.context.getBean(mainAreaClass);
      table.addRowClickListener(click -> {

      });
      return table;
   }

   @Override
   public String getMenuButtonCaption() {
      return messages.get(this, "caption");
   }

   protected Class<? extends Component> getHeaderAreaClass() {
      return null;
   }

   protected abstract Class<? extends Component> getMainAreaClass();

   protected abstract FontAwesome getMenuIcon();
}
