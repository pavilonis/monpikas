package lt.pavilonis.cmm.canteen.controllers;

import lt.pavilonis.cmm.canteen.domain.Meal;
import lt.pavilonis.cmm.canteen.domain.Pupil;
import lt.pavilonis.cmm.canteen.domain.PupilRepresentation;
import lt.pavilonis.cmm.canteen.repositories.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.repositories.MealRepository;
import lt.pavilonis.cmm.canteen.service.PupilService;
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
   private PupilService pupils;

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private MealEventLogRepository eventLogs;

   @ResponseBody
   @RequestMapping("mealRequest/{cardCode}")
   public ResponseEntity<PupilRepresentation> dinnerRequest(@PathVariable String cardCode) {

      LOG.info("Pupil meal request [cardCode={}]", cardCode);
      Optional<Pupil> pupilDto = pupils.find(cardCode);

      if (!pupilDto.isPresent()) {
         LOG.info("User NOT found in DB [cardCode={}]", cardCode);
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      Pupil dto = pupilDto.get();

      switch (dto.getMeals().size()) {

         case 0:
            LOG.info("Pupil has NO PERMISSION to have a meal [cardCode={}]", cardCode);
            PupilRepresentation clientDto = new PupilRepresentation(cardCode, dto.name(), null, dto.getGrade(), null);
            return new ResponseEntity<>(clientDto, HttpStatus.FORBIDDEN);

         case 1:
            return getMealResponse(dto, dto.getMeals().iterator().next());

         default:
            LocalTime now = now();
            Optional<Meal> meal = mealRepository.loadAll().stream()
                  .filter(mt -> mt.getStartTime().isBefore(now) && mt.getEndTime().isAfter(now))
                  .filter(mealByTime ->
                        dto.getMeals().stream().anyMatch(pupilMeal -> pupilMeal.getType() == mealByTime.getType()))
                  .findAny();

            return meal.isPresent()
                  ? getMealResponse(dto, meal.get())
                  : new ResponseEntity<>(HttpStatus.MULTIPLE_CHOICES);
      }
   }

   private ResponseEntity<PupilRepresentation> getMealResponse(Pupil pupil, Meal meal) {

      if (pupils.canHaveMeal(pupil.getCardCode(), new Date(), meal.getType())) {

         eventLogs.save(pupil.getCardCode(), pupil.name(), pupil.getGrade(), meal.getPrice(), meal.getType(), pupil.getType());

         LOG.info("OK - Pupil is getting meal [name={}, cardCode={}, mealType={}, price={}]",
               pupil.name(), pupil.getCardCode(), meal.getType().name(), meal.getPrice());

         return new ResponseEntity<>(
               new PupilRepresentation(pupil.getCardCode(), pupil.name(), meal, pupil.getGrade(), pupil.getType()),
               HttpStatus.ACCEPTED
         );

      } else {
         LOG.info("REJECT - Pupil already had his meal [name={}, cardCode={}]", pupil.name(), pupil.getCardCode());
         return new ResponseEntity<>(
               new PupilRepresentation(pupil.getCardCode(), pupil.name(), meal, pupil.getGrade(), pupil.getType()),
               HttpStatus.ALREADY_REPORTED
         );
      }
   }
}
