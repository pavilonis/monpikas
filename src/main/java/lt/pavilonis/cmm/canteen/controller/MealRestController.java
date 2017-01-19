package lt.pavilonis.cmm.canteen.controller;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.PupilRepresentation;
import lt.pavilonis.cmm.canteen.domain.UserMeal;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.repository.MealRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
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

import static java.time.LocalTime.now;
import static org.slf4j.LoggerFactory.getLogger;

@RequestMapping("rest")
@RestController
public class MealRestController {
   private static final Logger LOG = getLogger(MealRestController.class);

   @Autowired
   private UserMealService pupils;

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private MealEventLogRepository eventLogs;

   @ResponseBody
   @RequestMapping("mealRequest/{cardCode}")
   public ResponseEntity<PupilRepresentation> dinnerRequest(@PathVariable String cardCode) {

      LOG.info("Pupil meal request [cardCode={}]", cardCode);
      Optional<UserMeal> pupilDto = pupils.load(cardCode);

      if (!pupilDto.isPresent()) {
         LOG.info("User NOT found in DB [cardCode={}]", cardCode);
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      UserMeal dto = pupilDto.get();

      switch (dto.getMealData().getMeals().size()) {

         case 0:
            LOG.info("Pupil has NO PERMISSION to have a meal [cardCode={}]", cardCode);
            return new ResponseEntity<>(
                  new PupilRepresentation(
                        cardCode,
                        dto.getUser().getName(),
                        null,
                        dto.getUser().getGroup(),
                        null
                  ),
                  HttpStatus.FORBIDDEN
            );

         case 1:
            return getMealResponse(dto, dto.getMealData().getMeals().iterator().next());

         default:
            LocalTime now = now();
            Optional<Meal> meal = mealRepository.loadAll().stream()
                  .filter(mt -> mt.getStartTime().isBefore(now) && mt.getEndTime().isAfter(now))
                  .filter(mealByTime ->
                        dto.getMealData().getMeals().stream().anyMatch(pupilMeal -> pupilMeal.getType() == mealByTime.getType()))
                  .findAny();

            return meal.isPresent()
                  ? getMealResponse(dto, meal.get())
                  : new ResponseEntity<>(HttpStatus.MULTIPLE_CHOICES);
      }
   }

   private ResponseEntity<PupilRepresentation> getMealResponse(UserMeal userMeal, Meal meal) {

      if (pupils.canHaveMeal(userMeal.getUser().getCardCode(), new Date(), meal.getType())) {

         eventLogs.saveOrUpdate(
               new MealEventLog(
                     null,
                     //TODO create constructor which accepts userMeal, ...
                     userMeal.getUser().getCardCode(),
                     userMeal.getUser().getName(),
                     userMeal.getUser().getGroup(),
                     null,
                     meal.getPrice(),
                     meal.getType(),
                     userMeal.getMealData().getType()
               )
         );

         LOG.info("OK - Pupil is getting meal [name={}, cardCode={}, mealType={}, price={}]",
               userMeal.getUser().getName(), userMeal.getUser().getCardCode(), meal.getType().name(), meal.getPrice());

         return new ResponseEntity<>(
               new PupilRepresentation(
                     userMeal.getUser().getCardCode(),
                     userMeal.getUser().getName(),
                     meal,
                     userMeal.getUser().getGroup(),
                     userMeal.getMealData().getType()
               ),
               HttpStatus.ACCEPTED
         );

      } else {
         LOG.info("REJECT - Pupil already had his meal [name={}, cardCode={}]",
               userMeal.getUser().getName(), userMeal.getUser().getCardCode());

         return new ResponseEntity<>(
               new PupilRepresentation(
                     userMeal.getUser().getCardCode(),
                     userMeal.getUser().getName(),
                     meal,
                     userMeal.getUser().getGroup(),
                     userMeal.getMealData().getType()
               ),
               HttpStatus.ALREADY_REPORTED
         );
      }
   }
}

