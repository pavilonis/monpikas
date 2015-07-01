package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import lt.pavilonis.monpikas.server.dto.ClientPupilDto;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.MealRepository;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.ALREADY_REPORTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("rest")
@RestController
public class MealRestController {
   private static final Logger LOG = getLogger(MealRestController.class);

   @Autowired
   private PupilService pupilService;

   @Autowired
   private MealRepository mealRepository;

   @Autowired
   private MealEventLogRepository mealEventLogRepository;

   @ResponseBody
   @RequestMapping("mealRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {

      LOG.info("Pupil with id: " + id + " requested a meal");
      Optional<PupilDto> pupilDto = pupilService.getByCardId(id);

      if (pupilDto.isPresent()) {

         PupilDto dto = pupilDto.get();
         String name = dto.getFirstName() + " " + dto.getLastName();

         switch (dto.getMeals().size()) {

            case 0:
               LOG.info("Pupil with id: " + id + " has NO PERMISSION to have a meal");
               return new ResponseEntity<>(
                     new ClientPupilDto(dto.getAdbId(), name, null, dto.getGrade().orElse("")), FORBIDDEN);

            case 1:
               return getMealResponse(id, dto, name, dto.getMeals().iterator().next());

            default:
               LocalTime now = now();
               Optional<Meal> meal = mealRepository.findAll().stream()
                     .filter(mt -> mt.getStartTime().isBefore(now) && mt.getEndTime().isAfter(now))
                     .findAny()
                     .flatMap(mealByTime -> dto.getMeals().stream()
                           .filter(pupilsMeal -> pupilsMeal.getType() == mealByTime.getType())
                           .findAny());

               if (meal.isPresent()) {
                  return getMealResponse(id, dto, name, meal.get());

               } else {
                  LOG.error("MealTime not found!");
                  return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
               }

         }
      }

      LOG.info("Pupil with id: " + id + " was NOT found in ADB");
      return new ResponseEntity<>(NOT_FOUND);
   }


   private ResponseEntity<ClientPupilDto> getMealResponse(long id, PupilDto dto, String name, Meal meal) {

      if (pupilService.canHaveMeal(id, new Date(), meal.getType())) {
         mealEventLogRepository.save(
               new MealEventLog(
                     id, name, dto.getGrade().orElse(""), new Date(), meal.getPrice(), meal.getType(), dto.getPupilType()
               )
         );

         LOG.info("OK - Pupil '" + name + "' (id " + id + ") is getting " + meal.getType().name() + " for " + meal.getPrice() + " units of money");
         return new ResponseEntity<>(
               new ClientPupilDto(dto.getAdbId(), name, meal, dto.getGrade().orElse("")),
               ACCEPTED
         );

      } else {
         LOG.info("REJECT - Pupil '" + name + "' (id " + id + ") already had his meal");
         return new ResponseEntity<>(
               new ClientPupilDto(dto.getAdbId(), name, meal, dto.getGrade().orElse("")),
               ALREADY_REPORTED);
      }
   }
}
