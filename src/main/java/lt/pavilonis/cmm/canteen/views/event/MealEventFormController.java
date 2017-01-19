package lt.pavilonis.cmm.canteen.views.event;

import com.vaadin.data.Validator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FormView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MBeanFieldGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@UIScope
@SpringComponent
public class MealEventFormController extends AbstractFormController<MealEventLog, Long> {

   @Autowired
   private MealEventLogRepository eventLogRepository;

   @Autowired
   private UserMealService mealService;

   private MealEventFormView formView;

   @Override
   protected FormView<MealEventLog> getFormView() {
      List<UserMeal> selectionOptions = mealService.loadWithMealAssigned();
      return formView = new MealEventFormView(selectionOptions);
   }

   @Override
   protected void beforeSave(MealEventLog model) {
      UserMeal value = formView.getTableValue();
      if (value != null && model.getMealType() != null) {
         model.setName(value.getUser().getName());
         model.setCardCode(value.getUser().getCardCode());
         model.setGrade(value.getUser().getGroup());
         model.setPupilType(value.getMealData().getType());
         // TODO update (filter) pupil table values by selected meal type
         model.setPrice(
               value.getMealData()
                     .getMeals()
                     .stream()
                     .filter(portion -> portion.getType() == model.getMealType())
                     .findFirst()
                     .orElseThrow(() -> new RuntimeException("User does not have required meal assigned"))
                     .getPrice()
         );
      }
   }


   @Override
   protected Collection<MBeanFieldGroup.MValidator<MealEventLog>> getValidators() {
      return Collections.singletonList(value -> {
         //TODO add validation error component in abstract form
         try {
            tmpMethod(value);
         } catch (Validator.InvalidValueException e) {
            Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            throw e;
         }
      });
   }

   private void tmpMethod(MealEventLog value) {
      if (StringUtils.isBlank(value.getCardCode())) {
         throw new Validator.InvalidValueException("Nepasirinktas mokinys");
      } else if (value.getDate() == null) {
         throw new Validator.InvalidValueException("Nenurodyta data");
      } else if (!mealService.portionAssigned(value.getCardCode(), value.getMealType())) {
         throw new Validator.InvalidValueException("Mokinys neturi leidimo šio tipo maitinimuisi");
      } else if (!mealService.canHaveMeal(value.getCardCode(), value.getDate(), value.getMealType())) {
         throw new Validator.InvalidValueException("Viršijamas nurodytos dienos maitinimosi limitas");
      }
   }

   @Override
   protected EntityRepository<MealEventLog, Long> getEntityRepository() {
      return eventLogRepository;
   }

   @Override
   protected void customizeWindow(Window window) {
      window.setWidth(406, Unit.PIXELS);
      window.setHeight(580, Unit.PIXELS);
   }
}
