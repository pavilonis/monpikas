package lt.pavilonis.cmm.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fields.MTable;

import java.util.List;
import java.util.Optional;

import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;

public abstract class AbstractListController<T, ID> extends AbstractViewController {

   @Override
   protected Component getMainArea() {
      MTable<T> table = getTable()
            .withSize(MSize.FULL_SIZE);

      addTableListener(table);
      loadTableData(table);
      return table;
   }

   protected void loadTableData(MTable<T> table) {
      List<T> beans = getEntityRepository().loadAll();
      table.setBeans(beans);
   }

   protected void addTableListener(MTable<T> table) {

      getFormController().ifPresent(
            controller -> table.addRowClickListener(
                  click -> controller.edit(click.getRow())
            ));
   }

   protected abstract MTable<T> getTable();

   //TODO translate
   protected void actionDelete(MTable<T> table) {
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

   protected Optional<AbstractFormController<T, ID>> getFormController() {
      return Optional.empty();
   }

   protected abstract EntityRepository<T, ID> getEntityRepository();
}
