package lt.pavilonis.cmm.common;

import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import lt.pavilonis.cmm.common.ui.filter.FilterPanel;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T extends Identified<ID>, ID, FILTER> extends AbstractViewController {

   protected ListGrid<T> grid;
   protected FilterPanel<FILTER> filterPanel;

   @Override
   protected Component getMainArea() {
      grid = createGrid();
      grid.setSizeFull();

      addGridClickListener(grid);
      loadGridData(grid);
      return grid;
   }

   @Override
   final protected Component getHeader() {

      filterPanel = createFilterPanel();

      filterPanel.addSearchClickListener(click -> loadGridData(grid));

      filterPanel.addResetClickListener(click -> {
         filterPanel.fieldReset();
         loadGridData(grid);
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

   private void loadGridData(ListGrid<T> grid) {
      FILTER filter = filterPanel.getFilter();
      EntityRepository<T, ID, FILTER> repository = getEntityRepository();

      Optional<BackEndDataProvider<T, FILTER>> lazyDataProvider =
            repository.lazyDataProvider(filter);

      if (lazyDataProvider.isPresent()) {
         grid.setDataProvider(lazyDataProvider.get());
      } else {
         List<T> beans = repository.load(filter);
         grid.setItems(beans);
      }
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
