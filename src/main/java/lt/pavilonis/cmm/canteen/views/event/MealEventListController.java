package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ListTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.util.Date;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static lt.pavilonis.cmm.util.SecurityCheckUtils.hasRole;

@UIScope
@SpringComponent
public class MealEventListController extends AbstractListController<MealEventLog, Long> {

   @Autowired
   private MealEventTable table;

   @Autowired
   private UserMealService pupilService;

   @Autowired
   private MealEventLogRepository eventLogs;

   @Autowired
   private MealEventListFilterPanel filterPanel;

   @Autowired
   private MealEventFormController mealEventFormController;

   private void deleteAction() {
      if (!hasRole("ROLE_ADMIN")) {
         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
         return;
      }
      MealEventLog value = table.getValue();
      if (value == null) {
         show("Niekas nepasirinkta", WARNING_MESSAGE);
      } else {
         eventLogs.delete(value.getId());
         table.removeItem(value);
         table.select(null);
         show("Įrašas pašalintas", TRAY_NOTIFICATION);
      }
   }

//   private void addAction() {
//      if (!hasRole("ROLE_ADMIN")) {
//         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
//         return;
//      }
//   }
//
//   private void saveAction(MealEventManualCreateForm form) {
//      String cardCode = (String) form.getTable().getValue();
//      MealType type = form.getEventType();
//      if (valid(cardCode, form.getDate(), type)) {
//         UserMeal userMeal = pupilService.load(cardCode).get();
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
   protected void addTableClickListener(MTable<MealEventLog> table) {/*do nothing*/}

   @Override
   protected ListTable<MealEventLog> getTable() {
      return table;
   }

   @Override
   protected EntityRepository<MealEventLog, Long> getEntityRepository() {
      return eventLogs;
   }

   @Override
   protected Component getHeader() {
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
