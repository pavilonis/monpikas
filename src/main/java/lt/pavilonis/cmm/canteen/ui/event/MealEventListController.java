package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FilterPanel;
import lt.pavilonis.cmm.common.ListGrid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@UIScope
@SpringComponent
public class MealEventListController extends AbstractListController<MealEventLog, Long, MealEventFilter> {

   @Autowired
   private MealEventLogRepository eventLogs;

   @Autowired
   private MealEventFilterPanel filterPanel;

   @Autowired
   private MealEventFormController mealEventFormController;

//   private void deleteAction() {
//      if (!hasRole("ROLE_ADMIN")) {
//         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
//         return;
//      }
//      MealEventLog value = table.getValue();
//      if (value == null) {
//         show("Niekas nepasirinkta", WARNING_MESSAGE);
//      } else {
//         eventLogs.delete(value.getId());
//         table.removeItem(value);
//         table.select(null);
//         show("Įrašas pašalintas", TRAY_NOTIFICATION);
//      }
//   }

//   private void addAction() {
//      if (!hasRole("ROLE_ADMIN")) {
//         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
//         return;
//      }
//   }
//
//   private void saveAction(MealEventManualCreateForm form) {
//      String cardCode = (String) form.createGrid().getValue();
//      MealType type = form.getEventType();
//      if (valid(cardCode, form.getDate(), type)) {
//         UserMeal userMeal = pupilService.find(cardCode).get();
//
//         BigDecimal price = userMeal
//               .getMealData()
//               .getMeals()
//               .stream()
//               .filter(portion -> portion.getType() == type)
//               .findFirst()
//               .get()
//               .getPrice();
//
//         MealEventLog log = eventLogs.saveOrUpdate(
//               new MealEventLog(
//                     null,
//                     userMeal.getUser().getCardCode(),
//                     userMeal.getUser().getName(),
//                     userMeal.getUser().getGroup(),
//                     null,
//                     price,
//                     type,
//                     userMeal.getMealData().getType()
//
//               )
//         );
//         table.addBeans(log);
////         form.close();
//         show("Išsaugota", TRAY_NOTIFICATION);
//      }
//   }

   @Override
   protected void addGridClickListener(ListGrid<MealEventLog> table) {/*do nothing*/}

   @Override
   protected ListGrid<MealEventLog> createGrid() {
      return new MealEventGrid();
   }

   @Override
   protected EntityRepository<MealEventLog, Long, MealEventFilter> getEntityRepository() {
      return eventLogs;
   }

   @Override
   protected FilterPanel<MealEventFilter> createFilterPanel() {
      return filterPanel;
   }

   @Override
   protected Class<MealEventLog> getEntityClass() {
      return MealEventLog.class;
   }

   @Override
   protected AbstractFormController<MealEventLog, Long> getFormController() {
      return mealEventFormController;
   }

   @Override
   protected MealEventLog createNewInstance() {
      MealEventLog entity = new MealEventLog();
      entity.setDate(new Date());
      entity.setMealType(MealType.DINNER);
      return entity;
   }
}
