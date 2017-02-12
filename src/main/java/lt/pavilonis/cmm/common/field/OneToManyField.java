package lt.pavilonis.cmm.common.field;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
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
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OneToManyField<T> extends CustomField<List> {

   private final ListTable<T> table;
   private final Class<T> type;

   public OneToManyField(Class<T> type) {
      this.table = createTable(type);
      this.type = type;
   }

   protected ListTable<T> createTable(Class<T> type) {
      ListTable<T> table = new ListTable<>(type);

      List<String> properties = collectProperties(type);

      table.withProperties(properties);
      table.withSize(MSize.size("550px", "350px"));
      return table;
   }

   private List<String> collectProperties(Class<T> type) {
      return Stream.of(type.getMethods())
            .map(Method::getName)
            .filter(name -> name.startsWith("get") || name.startsWith("is"))
            .map(name -> name.startsWith("is") ? name.substring(2) : name.substring(3))
            .map(String::toLowerCase)
            .collect(Collectors.toList());
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
      Consumer<T> selectionConsumer = t -> {
         if (table.getItemIds().contains(t)) {
            Notification.show("Already in the list!", Type.WARNING_MESSAGE);
         } else {
            table.addBeans(Collections.singletonList(t));
         }
      };

      new SelectionPopup(selectionConsumer);
   }

   private void actionRemove() {
      T selected = table.getValue();
      if (selected == null) {
         Notification.show("Nothing selected!", Type.WARNING_MESSAGE);
      } else {
         table.getItemIds().remove(selected);
         table.refreshRows();
      }
   }

   protected List<T> getSelectionElements() {
      EntityRepository<T, ?, ?> repository = RepositoryFinder.find(type);
      return repository.loadAll(null);
   }

   @Override
   protected void setInternalValue(List newValue) {
      super.setInternalValue(newValue);
      table.setBeans(newValue);
   }

   @Override
   public void setValue(List newFieldValue) throws ReadOnlyException, Converter.ConversionException {
      super.setValue(newFieldValue);
   }

   @Override
   public List<T> getValue() {
      return (List) table.getItemIds();
   }

   @Override
   public Property getPropertyDataSource() {
      return super.getPropertyDataSource();
   }

   @Override
   public Class<? extends List> getType() {
      return List.class;
   }

   private class SelectionPopup extends MWindow {

      public SelectionPopup(Consumer<T> selectionConsumer) {

         setCaption(App.translate(SelectionPopup.class, "caption"));
         withSize(MSize.size("700px", "490px"));

         ListTable<T> selectionTable = new ListTable<>(type);
         selectionTable.setBeans(getSelectionElements());
         selectionTable.withProperties(collectProperties(type));
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
}
