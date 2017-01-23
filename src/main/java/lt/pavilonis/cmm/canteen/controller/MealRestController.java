package lt.pavilonis.cmm.canteen.controller;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilRepresentation;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.domain.UserRepresentation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static java.time.LocalTime.now;
import static org.slf4j.LoggerFactory.getLogger;

@RequestMapping("rest")
@RestController
public class MealRestController {
   private static final Logger LOG = getLogger(MealRestController.class);

   @Autowired
   private UserMealService userMealService;

   @Autowired
   private MealEventLogRepository eventLogs;

   @ResponseBody
   @RequestMapping("mealRequest/{cardCode}")
   public ResponseEntity<PupilRepresentation> dinnerRequest(@PathVariable String cardCode) {

      LOG.info("Pupil meal request [cardCode={}]", cardCode);
      Optional<UserMeal> optionalUserMeal = userMealService.load(cardCode);

      if (!optionalUserMeal.isPresent()) {
         LOG.info("User NOT found in DB [cardCode={}]", cardCode);
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      UserMeal userMeal = optionalUserMeal.get();
      Set<Meal> meals = userMeal.getMealData().getMeals();

      if (meals.isEmpty()) {
         return forbidden(userMeal);

      } else if (meals.size() == 1) {
         return successIfFirstTimePerDay(userMeal, meals.iterator().next());

      } else {
         LocalTime now = now();
         return meals.stream()
               .filter(m -> m.getStartTime().isBefore(now))
               .filter(m -> m.getEndTime().isAfter(now))
               .map(m -> successIfFirstTimePerDay(userMeal, m))
               .findFirst()
               .orElseGet(() -> new ResponseEntity<>(HttpStatus.MULTIPLE_CHOICES)); // TODO bad return status?
      }
   }

   private ResponseEntity<PupilRepresentation> forbidden(UserMeal userMeal) {
      UserRepresentation user = userMeal.getUser();
      LOG.info("Pupil has NO PERMISSION to have a meal [cardCode={}]", user.getCardCode());

      PupilRepresentation response =
            new PupilRepresentation(user.getCardCode(), user.getName(), null, user.getGroup(), null);
      return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
   }

   private ResponseEntity<PupilRepresentation> successIfFirstTimePerDay(UserMeal userMeal, Meal meal) {

      UserRepresentation user = userMeal.getUser();
      PupilType pupilType = userMeal.getMealData().getType();
      String cardCode = user.getCardCode();
      String name = user.getName();
      MealType mealType = meal.getType();

      PupilRepresentation response =
            new PupilRepresentation(cardCode, name, meal, user.getGroup(), pupilType);

      if (!userMealService.canHaveMeal(cardCode, new Date(), mealType)) {
         LOG.info("REJECT - Pupil already had his meal [name={}, cardCode={}]", name, cardCode);
         return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
      }

      eventLogs.saveOrUpdate(new MealEventLog(
            null, cardCode, name, user.getGroup(), new Date(), meal.getPrice(), mealType, pupilType
      ));

      LOG.info("OK - Pupil is getting meal [name={}, cardCode={}, mealType={}, price={}]",
            name, cardCode, mealType.name(), meal.getPrice());

      return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
   }
}

