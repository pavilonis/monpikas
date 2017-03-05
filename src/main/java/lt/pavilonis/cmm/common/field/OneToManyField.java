package lt.pavilonis.cmm.common.field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.common.component.TableControlPanel;
import lt.pavilonis.cmm.repository.RepositoryFinder;
import org.apache.commons.collections4.CollectionUtils;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class OneToManyField<T> extends CustomField<List> {

   private final ListTable<T> table;
   private final List<T> value = new ArrayList<>();
   private final Class<T> type;

   public OneToManyField(Class<T> type) {
      this.table = createTable(type);
      this.table.setDataProvider(
            (sortOrder, offset, limit) -> value.stream(),
            value::size
      );
      this.type = type;
   }

   @Override
   protected void doSetValue(List value) {

   }

   protected ListTable<T> createTable(Class<T> type) {
      ListTable<T> table = new ListTable<>(type);
      setWidth(550, Unit.PIXELS);
      setHeight(350, Unit.PIXELS);
      return table;
   }


   @Override
   protected Component initContent() {
      return new MVerticalLayout(
            table,
            new TableControlPanel(
                  eventAdd -> actionAdd(),
                  eventRemove -> actionRemove()
            )
      ).withMargin(false);
   }

   private void actionAdd() {
      Consumer<Set<T>> selectionConsumer = items -> {
         boolean duplicatesFound = false;
         for (T item : items) {
            if (value.contains(items)) {
               duplicatesFound = true;
            } else {
               value.add(item);
            }
         }
         if (duplicatesFound) {
            Notification.show("Some values not added (already in the list)", Type.WARNING_MESSAGE);
         }
      };

      new SelectionPopup(selectionConsumer);
   }

   private void actionRemove() {
      Set<T> selectedItems = table.getSelectedItems();
      if (CollectionUtils.isEmpty(selectedItems)) {
         Notification.show("Nothing selected!", Type.WARNING_MESSAGE);
      } else {
         value.removeAll(selectedItems);
         table.getDataProvider().refreshAll();
      }
   }

   protected List<T> getSelectionElements() {
      EntityRepository<T, ?, ?> repository = RepositoryFinder.find(type);
      return repository.loadAll(null);
   }

   @Override
   public List<T> getValue() {
      return value;
   }

   private class SelectionPopup extends MWindow {

      private SelectionPopup(Consumer<Set<T>> selectionConsumer) {

         setCaption(App.translate(SelectionPopup.class, "caption"));
         withSize(MSize.size("700px", "490px"));

         ListTable<T> selectionTable = new ListTable<>(type);
         selectionTable.setDataProvider(
               (sortOrder, offset, limit) -> getSelectionElements().stream(),
               () -> getSelectionElements().size()
         );
         selectionTable.addItemClickListener(click -> {
            if (click.getMouseEventDetails().isDoubleClick()) {
               selectAction(selectionConsumer, Collections.singleton(click.getItem()));
            }
         });

         setContent(
               new MVerticalLayout(
                     selectionTable,
                     new TableControlPanel(
                           "addSelected", "close",
                           click -> selectAction(selectionConsumer, selectionTable.getSelectedItems()),
                           click -> close()
                     )
               )
                     .withSize(MSize.FULL_SIZE)
                     .expand(selectionTable)
         );
         setModal(true);

         UI.getCurrent().addWindow(this);
      }

      protected void selectAction(Consumer<Set<T>> selectionConsumer, Set<T> selected) {
         if (CollectionUtils.isNotEmpty(selected)) {
            selectionConsumer.accept(selected);
         }
         close();
      }
   }

   public void setTableHeight(float value, Unit unit) {
      table.setHeight(value, unit);
   }
}
