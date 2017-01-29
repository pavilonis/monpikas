package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.UI;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.common.component.TableControlPanel;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.function.Consumer;

public class OneToManyFieldSelectionPopup<T> extends MWindow {

   public OneToManyFieldSelectionPopup(Consumer<T> selectionConsumer, Class<T> type) {

      setCaption(App.translate(this, "caption"));
      withSize(MSize.size("700px", "490px"));

      ListTable<T> selectionTable = new ListTable<>(type);
//      selectionTable.setPageLength(10);
      selectionTable.collapseColumns();
      selectionTable.addRowClickListener(click -> {
         if (click.isDoubleClick()) {
            selectAction(selectionConsumer, click.getRow());
         }
      });

      setContent(
            new MVerticalLayout(
                  selectionTable,
                  new TableControlPanel(
                        "addSelected", "close",
                        click -> selectAction(selectionConsumer, selectionTable.getValue()),
                        click -> close()
                  )
            )
//                  .withMargin(false)
                  .withSize(MSize.FULL_SIZE)
                  .expand(selectionTable)
      );
      setModal(true);

      UI.getCurrent().addWindow(this);
   }

   protected void selectAction(Consumer<T> selectionConsumer, T selectedValue) {
      if (selectedValue != null) {
         selectionConsumer.accept(selectedValue);
      }
      close();
   }
}