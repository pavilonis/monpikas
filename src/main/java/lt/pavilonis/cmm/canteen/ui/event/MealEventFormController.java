package lt.pavilonis.cmm.canteen.ui.event;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Window;
import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.common.AbstractFormController;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.FieldLayout;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@UIScope
@SpringComponent
public class MealEventFormController extends AbstractFormController<MealEventLog, Long> {

   public MealEventFormController() {
      super(MealEventLog.class);
   }

   @Autowired
   private MealEventLogRepository eventLogRepository;

   @Autowired
   private UserMealService mealService;

   private MealEventFormView formView;

   @Override
   protected FieldLayout<MealEventLog> createFieldLayout() {
      return formView = new MealEventFormView(mealService);
   }

   @Override
   protected void beforeSave(MealEventLog model) {
      Set<UserMeal> value = formView.getGridSelection();

      if (CollectionUtils.isNotEmpty(value)
            && value.size() == 1
            && model.getMealType() != null) {

         UserMeal selectedUserMeal = value.iterator().next();

         Meal meal = selectedUserMeal.getMealData()
               .getMeals()
               .stream()
               .filter(portion -> portion.getType() == model.getMealType())
               .findFirst()
               .orElseThrow(() -> new RuntimeException("User does not have required meal assigned"));

         model.setDate(correctMealEventTime(model.getDate(), meal));
         model.setName(selectedUserMeal.getUser().getName());
         model.setCardCode(selectedUserMeal.getUser().getCardCode());
         model.setGrade(selectedUserMeal.getUser().getGroup());
         model.setPupilType(selectedUserMeal.getMealData().getType());
         model.setPrice(meal.getPrice());
      }
   }

   private Date correctMealEventTime(Date date, Meal meal) {
      return LocalDateTime.fromDateFields(date)
            .withTime(0, 0, 0, 0)
            .plusSeconds(meal.getStartTime().toSecondOfDay())
            .toDate();
   }

   @Override
   protected Collection<Validator<MealEventLog>> getValidators() {
      return Arrays.asList(
            (value, context) -> mealService.portionAssigned(value.getCardCode(), value.getMealType())
                  ? ValidationResult.ok()
                  : ValidationResult.error("Mokinys neturi leidimo šio tipo maitinimuisi"),
            (value, context) -> mealService.canHaveMeal(value.getCardCode(), value.getDate(), value.getMealType())
                  ? ValidationResult.ok()
                  : ValidationResult.error("Viršijamas nurodytos dienos maitinimosi limitas")
      );
   }

   @Override
   protected EntityRepository<MealEventLog, Long, ?> getEntityRepository() {
      return eventLogRepository;
   }

   @Override
   protected void customizeWindow(Window window) {
      window.setWidth(430, Unit.PIXELS);
      window.setHeight(600, Unit.PIXELS);
   }
}
