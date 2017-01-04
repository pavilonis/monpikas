package lt.pavilonis.cmm.canteen.views.mealevents;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.repositories.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.math.BigDecimal;
import java.util.Date;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.TRAY_NOTIFICATION;
import static com.vaadin.ui.Notification.Type.WARNING_MESSAGE;
import static com.vaadin.ui.Notification.show;
import static lt.pavilonis.cmm.util.SecurityCheckUtils.hasRole;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@UIScope
@SpringComponent
public class MealEventListView extends MVerticalLayout {

   private final BeanContainer<Long, MealEventLog> container = new BeanContainer<>(MealEventLog.class);
   private final MealEventTable table;

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MealEventLogRepository eventLogs;

   @Autowired
   public MealEventListView(MessageSourceAdapter messages,
                            MealEventListFilterPanel filterPanel, MealEventTable table) {
      this.table = table;
//      setSizeFull();
      add(
            filterPanel,
            table,
            new MHorizontalLayout(
                  new MButton(FontAwesome.PLUS, messages.get(this, "buttonAdd"), click -> addAction()),
                  new MButton(FontAwesome.WARNING, messages.get(this, "buttonDelete"), click -> deleteAction())
                        .withStyleName("redicon")
            ).withMargin(false)
      );
      expand(table);
      setMargin(false);
   }

   private void deleteAction() {
      if (!hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {
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
      if (!hasRole(getContext().getAuthentication(), "ROLE_ADMIN")) {
         show("Veiksmas negalimas: truksta teisių", ERROR_MESSAGE);
         return;
      }
      MealEventManualCreateForm form = new MealEventManualCreateForm();
      form.getContainer().addAll(pupilService.loadWithMealAssigned());
      form.addCloseButtonListener(closeClick -> form.close());
      form.addSaveButtonListener(saveClick -> {
         String cardCode = (String) form.getTable().getValue();
         MealType type = form.getEventType();
         if (valid(cardCode, form.getDate(), type)) {
            Pupil pupil = pupilService.find(cardCode).get();

            BigDecimal price = pupil.getMeals().stream()
                  .filter(portion -> portion.getType() == type)
                  .findFirst()
                  .get()
                  .getPrice();

            MealEventLog log = eventLogs.save(
                  pupil.getCardCode(),
                  pupil.name(),
                  pupil.getGrade(),
                  price,
                  type,
                  pupil.getType()
            );
            container.addBean(log);
            form.close();
            show("Išsaugota", TRAY_NOTIFICATION);
         }
      });
      UI.getCurrent().addWindow(form);
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
}
