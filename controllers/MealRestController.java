package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.dto.ClientPupilDto;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.time.LocalTime.parse;
import static org.slf4j.LoggerFactory.getLogger;

@RequestMapping("rest")
@RestController
public class MealRestController {
   private static final Logger LOG = getLogger(MealRestController.class);

   @Value("${time.midday}")
   private String midday;

   @Autowired
   PupilService pupilService;

   @Autowired
   MealService mealService;

   @ResponseBody
   @RequestMapping("mealRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {

      LOG.info("Pupil with id: " + id + " requested a meal");
      Optional<AdbPupilDto> dto = pupilService.getByCardId(id);


      if (!dto.isPresent()) {
         LOG.info("Pupil with id: " + id + " was NOT found in ADB");
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

      } else {

         String name = dto.get().getFirstName() + " " + dto.get().getLastName();

         switch (pupilService.numOfPortionAssigned(dto.get())) {

            case 0:
               LOG.info("Pupil with id: " + id + " has NO PERMISSION to have a meal");
               return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), name, false, false), HttpStatus.OK);

            case 1:
               Portion portion = pupilService.onlyPortionFor(dto.get());
               if (pupilService.canHaveMeal(id, new Date(), portion.getType())) {

                  mealService.saveMealEvent(
                        new MealEvent(id, name, new Date(), portion.getPrice(), portion.getType())
                  );

                  //TODO store these messages in message container
                  LOG.info("OK - Pupil '" + name + "' (id " + id + ") is getting " +
                        portion.getType().name() + " for " + portion.getPrice() + "units of money");

                  return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), name, true, false), HttpStatus.OK);

               } else {

                  LOG.info("REJECT - Pupil '" + name + "' (id " + id + ") already had his meal");
                  return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), name, true, true), HttpStatus.OK);

               }

            case 2:
               //TODO parsing every time?
               portion = now().isBefore(parse(midday))
                     ? dto.get().getBreakfastPortion().get()
                     : dto.get().getDinnerPortion().get();

               if (pupilService.canHaveMeal(id, new Date(), portion.getType())) {

                  mealService.saveMealEvent(
                        new MealEvent(id, name, new Date(), portion.getPrice(), portion.getType())
                  );

                  LOG.info("OK - Pupil '" + name + "' (id " + id + ") is getting " +
                        portion.getType().name() + " for " + portion.getPrice() + "units of money");

                  return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), name, true, false), HttpStatus.OK);
               } else {

                  LOG.info("REJECT - Pupil '" + name + "' (id " + id + ") already had his meal");
                  return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), name, true, true), HttpStatus.OK);

               }

            default:
               LOG.error("found more than 3 meals assigned?");
               return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
      }
   }
}
