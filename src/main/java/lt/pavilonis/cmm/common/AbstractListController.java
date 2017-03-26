package lt.pavilonis.cmm.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T extends Identifiable<ID>, ID, FILTER> extends AbstractViewController {

   protected ListGrid<T> grid;
   protected FilterPanel<FILTER> filterPanel;

   @Override
   protected Component getMainArea() {
      grid = createGrid();
      grid.setSizeFull();

      addGridClickListener(grid);
      loadTableData(grid);
      return grid;
   }

   @Override
   final protected Component getHeader() {

      filterPanel = createFilterPanel();

      filterPanel.addSearchClickListener(click -> {
         loadTableData(grid);
      });

      filterPanel.addResetClickListener(click -> {
         filterPanel.fieldReset();
         loadTableData(grid);
      });

      return filterPanel;
   }

   @Override
   final protected Component getFooter() {
      return getControlPanel();
   }

   protected Component getControlPanel() {
      return new ControlPanel(click -> actionCreate(), click -> actionDelete());
   }

   private void loadTableData(ListGrid<T> table) {
      FILTER filter = filterPanel.getFilter();
      EntityRepository<T, ID, FILTER> repository = getEntityRepository();

      List<T> beans = repository.loadAll(filter);
      table.setItems(beans);
      table.collapseColumns();
//      grid.sort();
   }

   protected void addGridClickListener(ListGrid<T> table) {

      AbstractFormController<T, ID> formController = getFormController();

      if (formController == null) {
         return;
      }

      table.addItemClickListener(click -> {
         if (click.getMouseEventDetails().isDoubleClick()) {
            formController.edit(click.getItem(), table);
         }
      });
   }

   protected abstract ListGrid<T> createGrid();

   protected void actionCreate() {
      T entity = createNewInstance();
      getFormController().edit(entity, grid);
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
      Set<T> selected = grid.getSelectedItems();
      if (CollectionUtils.isEmpty(selected)) {
         Notification.show("Niekas nepasirinkta", WARNING_MESSAGE);
      } else {
         selected.forEach(item -> {
            getEntityRepository().delete(item.getId());
            grid.removeItem(item);
         });
         grid.deselectAll();
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
