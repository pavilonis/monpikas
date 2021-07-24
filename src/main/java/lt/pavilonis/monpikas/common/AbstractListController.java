package lt.pavilonis.monpikas.common;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import lt.pavilonis.monpikas.common.ui.filter.FilterPanel;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T extends Identified<ID>, ID, FILTER> extends AbstractViewController {

   protected ListGrid<T> grid;
   protected FilterPanel<FILTER> filterPanel;
   private final Label sizeLabel = new Label(null, ContentMode.HTML);

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
   final protected Optional<Component> getFooter(Component mainArea) {
      var footerLayout = new HorizontalLayout();
      footerLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);

      getControlPanel(mainArea).ifPresent(panel -> {
         footerLayout.addComponent(panel);
         footerLayout.setComponentAlignment(panel, Alignment.MIDDLE_LEFT);
      });

      footerLayout.addComponent(sizeLabel);
      footerLayout.setComponentAlignment(sizeLabel, Alignment.MIDDLE_RIGHT);
      return Optional.of(footerLayout);
   }

   protected Optional<Component> getControlPanel(Component mainArea) {
      var controls = new ControlPanel(click -> actionCreate(), click -> actionDelete());
      return Optional.of(controls);
   }

   private void loadGridData(ListGrid<T> grid) {

      FILTER filter = filterPanel.getFilter();
      EntityRepository<T, ID, FILTER> repository = getEntityRepository();

      Optional<SizeConsumingBackendDataProvider<T, FILTER>> lazyDataProvider =
            repository.lazyDataProvider(filter);

      if (lazyDataProvider.isPresent()) {

         Consumer<Integer> sizeConsumer = size -> sizeLabel.setValue(badgeHtml(size));
         lazyDataProvider.get().setSizeConsumer(sizeConsumer);

         grid.setDataProvider(lazyDataProvider.get());

      } else {

         List<T> beans = repository.load(filter);
         sizeLabel.setValue(badgeHtml(beans.size()));
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
            formController.edit(click.getItem(), table, false);
         }
      });
   }

   protected abstract ListGrid<T> createGrid();

   protected void actionCreate() {
      T entity = createNewInstance();
      getFormController().edit(entity, grid, false);
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

   private String badgeHtml(int size) {
      return "<span class='row-number-badge'>" + size + "</span>";
   }
}
