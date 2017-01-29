package lt.pavilonis.cmm.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.viritin.fields.MTable;

import java.util.List;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T extends Identifiable<ID>, ID, FILTER> extends AbstractViewController {

   ListTable<T> table;
   FilterPanel<FILTER> filterPanel;

   @Override
   protected Component getMainArea() {
      table = createTable();
      table.setSizeFull();

      addTableClickListener(table);
      loadTableData(table);
      return table;
   }

   @Override
   final protected Component getHeader() {

      filterPanel = createFilterPanel();

      filterPanel.addSearchClickListener(click -> {
         loadTableData(table);
      });

      filterPanel.addResetClickListener(click -> {
         filterPanel.fieldReset();
         loadTableData(table);
      });

      return filterPanel;
   }

   @Override
   final protected Component getFooter() {
      return getControlPanel();
   }

   protected Component getControlPanel() {
      return new ControlPanel(
            messageSource,
            click -> actionCreate(),
            click -> actionDelete()
      );
   }

   private void loadTableData(ListTable<T> table) {
      FILTER filter = filterPanel.getFilter();
      EntityRepository<T, ID, FILTER> repository = getEntityRepository();

      List<T> beans = repository.loadAll(filter);
      table.setBeans(beans);
      table.collapseColumns();
      table.sort();
   }

   protected void addTableClickListener(MTable<T> table) {

      AbstractFormController<T, ID> formController = getFormController();

      if (formController != null) {
         table.addRowClickListener(click -> {
            if (click.isDoubleClick()) {
               formController.edit(click.getRow(), table);
            }
         });
      }
   }

   protected abstract ListTable<T> createTable();

   protected void actionCreate() {
      T entity = createNewInstance();
      getFormController().edit(entity, table);
   }

   protected T createNewInstance() {
      try {
         return getEntityClass().newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
         throw new RuntimeException("Can not instantiate " + getEntityClass().getSimpleName() + ". " + e);
      }
   }

   //TODO translate
   protected void actionDelete() {
      T selected = table.getValue();
      if (selected == null) {
         Notification.show("Niekas nepasirinkta", WARNING_MESSAGE);
      } else {
         getEntityRepository().delete(selected.getId());
         table.removeItem(selected);
         table.select(null);
         Notification.show("Įrašas pašalintas", TRAY_NOTIFICATION);
      }
   }

   protected AbstractFormController<T, ID> getFormController() {
      return null;
   }

   protected abstract FilterPanel<FILTER> createFilterPanel();

   protected abstract EntityRepository<T, ID, FILTER> getEntityRepository();

   protected abstract Class<T> getEntityClass();
}
