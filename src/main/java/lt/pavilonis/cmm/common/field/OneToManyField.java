package lt.pavilonis.cmm.common.field;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import lt.pavilonis.cmm.common.ListTable;
import lt.pavilonis.cmm.common.component.TableControlPanel;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class OneToManyField<T> extends CustomField<List> {

   private final ListTable<T> table;
   private final Class<T> type;

   public OneToManyField(Class<T> type) {
      this.table = createTable(type);
      this.type = type;
   }

   protected ListTable<T> createTable(Class<T> type) {
      ListTable<T> table = new ListTable<>(type);
      table.withSize(MSize.size("550px", "350px"));
//      table.setSizeFull();
      return table;
   }

   @Override
   protected Component initContent() {

      Button.ClickListener listenerAdd = eventAdd -> {
         Consumer<T> selectionConsumer = t -> {
            if (table.getItemIds().contains(t)) {
               Notification.show("Already in the list!", Type.WARNING_MESSAGE);
            } else {
               table.addBeans(Collections.singletonList(t));
            }
         };
         new OneToManyFieldSelectionPopup<>(selectionConsumer, type);
      };

      Button.ClickListener listenerRemove = eventRemove -> {
         T selected = table.getValue();
         if (selected == null) {
            Notification.show("Nothing selected!", Type.WARNING_MESSAGE);
         } else {
            table.getItemIds().remove(selected);
         }
      };

      return new MVerticalLayout(table, new TableControlPanel(listenerAdd, listenerRemove))
            .withMargin(false);
   }

   protected List<T> getSelectorEntities() {
      //TODO auto load selector elements by type
//      EntityRepository<T, ID, FILTER> repository = RepositoryFinder.find(type);
//      return repository.loadAll();
      return Collections.emptyList();
   }

   @Override
   protected void setInternalValue(List newValue) {
      super.setInternalValue(newValue);
   }

   @Override
   public void setValue(List newFieldValue) throws ReadOnlyException, Converter.ConversionException {
      table.setBeans(newFieldValue);
   }

   @Override
   public List getValue() {
      return (List) table.getItemIds();
   }

   @Override
   public Class<? extends List> getType() {
      return List.class;
   }
}
