package lt.pavilonis.cmm.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.viritin.fields.MTable;

import java.util.List;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T extends Identifiable<ID>, ID> extends AbstractViewController {

   ListTable<T> table;

   @Override
   protected Component getMainArea() {
      table = getTable();
      table.setSizeFull();

      addTableClickListener(table);
      loadTableData(table);
      return table;
   }

   @Override
   protected Component getFooter() {
      return getControlPanel();
   }

   protected Component getControlPanel() {
      return new ControlPanel(
            messages,
            click -> actionCreate(),
            click -> actionDelete()
      );
   }

   protected void loadTableData(ListTable<T> table) {
      List<T> beans = getEntityRepository().loadAll();
      table.addBeans(beans);
      table.columnsToCollapse()
            .forEach(columnId -> table.setColumnCollapsed(columnId, true));
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

   protected abstract ListTable<T> getTable();

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
         table.removeItem(selected);
         table.select(null);
         Notification.show("Įrašas pašalintas", TRAY_NOTIFICATION);
//         List<UserMeal> portionUsers = userMealService.loadByMeal(selected.getId());
//         if (portionUsers.isEmpty()) {
//            mealRepository.delete(selected.getId());
//         } else {
//            Notification.show("Porciją priskirta šioms kortelėms:", portionUsers.toString(), ERROR_MESSAGE);
//         }
      }
   }

   protected AbstractFormController<T, ID> getFormController() {
      return null;
   }

   protected abstract EntityRepository<T, ID> getEntityRepository();

   protected abstract Class<T> getEntityClass();
}
