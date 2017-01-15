package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractListController;
import lt.pavilonis.cmm.common.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static lt.pavilonis.cmm.util.SecurityCheckUtils.hasRole;

@UIScope
@SpringComponent
public class MealEventListView extends AbstractListController<MealEventLog, Long> {

   @Autowired
   private MealEventTable table;

   @Autowired
   private UserMealService pupilService;

   @Autowired
   private MealEventLogRepository eventLogs;

   @Autowired
   private MealEventListFilterPanel filterPanel;

//   @Autowired
//   public MealEventListView(MessageSourceAdapter messages,
//                            MealEventListFilterPanel filterPanel, MealEventTable table) {
//      this.table = table;
//      add(
//            filterPanel,
//            table,
//            new MHorizontalLayout(
//                  new MButton(FontAwesome.PLUS, messages.get(this, "buttonAdd"), click -> addAction()),
//                  new MButton(FontAwesome.WARNING, messages.get(this, "buttonDelete"), click -> deleteAction())
//                        .withStyleName("redicon")
//            ).withMargin(false)
//      );
//      expand(table);
//      setMargin(false);
//   }

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

   private void addAction() {
      if (!hasRole("ROLE_ADMIN")) {
         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
         return;
      }
      MealEventManualCreateForm form = new MealEventManualCreateForm(
            pupilService.loadWithMealAssigned()
      );
      form.addCloseButtonListener(closeClick -> form.close());
      form.addSaveButtonListener(saveClick -> saveAction(form));
      UI.getCurrent().addWindow(form);
   }

   private void saveAction(MealEventManualCreateForm form) {
      String cardCode = (String) form.getTable().getValue();
      MealType type = form.getEventType();
      if (valid(cardCode, form.getDate(), type)) {
         UserMeal userMeal = pupilService.load(cardCode).get();

         BigDecimal price = userMeal
               .getMealData()
               .getMeals()
               .stream()
               .filter(portion -> portion.getType() == type)
               .findFirst()
               .get()
               .getPrice();

         MealEventLog log = eventLogs.saveOrUpdate(
               new MealEventLog(
                     null,
                     userMeal.getUser().getCardCode(),
                     userMeal.getUser().getName(),
                     userMeal.getUser().getGroup(),
                     null,
                     price,
                     type,
                     userMeal.getMealData().getType()

               )
         );
         table.addBeans(log);
         form.close();
         show("Išsaugota", TRAY_NOTIFICATION);
      }
   }

   private boolean valid(String cardCode, Date date, MealType mealType) {
      if (cardCode == null) {
         show("Nepasirinktas mokinys", WARNING_MESSAGE);
         return false;
      } else if (date == null) {
         show("Nenurodyta data", WARNING_MESSAGE);
         return false;
      } else if (!pupilService.portionAssigned(cardCode, mealType)) {
         show("Mokinys neturi leidimo šio tipo maitinimuisi", ERROR_MESSAGE);
         return false;
      } else if (!pupilService.canHaveMeal(cardCode, date, mealType)) {
         show("Viršijamas nurodytos dienos maitinimosi limitas", ERROR_MESSAGE);
         return false;
      } else {
         return true;
      }
   }

   @Override
   protected MTable<MealEventLog> getTable() {
      return table;
   }


   @Override
   protected EntityRepository<MealEventLog, Long> getEntityRepository() {
      return eventLogs;
   }

   @Override
   protected Optional<Component> getHeader() {
      return Optional.of(filterPanel);
   }
}
