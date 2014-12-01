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
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.ALREADY_REPORTED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
      Optional<AdbPupilDto> pupilDto = pupilService.getByCardId(id);

      if (pupilDto.isPresent()) {

         AdbPupilDto dto = pupilDto.get();
         String name = dto.getFirstName() + " " + dto.getLastName();

         switch (pupilService.numOfPortionAssigned(dto)) {

            case 0:
               LOG.info("Pupil with id: " + id + " has NO PERMISSION to have a meal");
               return new ResponseEntity<>(new ClientPupilDto(id, name, null, dto.getGrade().orElse("")), FORBIDDEN);

            case 1:
               Portion portion = pupilService.onlyPortionFor(dto);
               return getMealResponse(id, dto, name, portion);

            case 2:
               //TODO parsing every time?
               portion = now().isBefore(parse(midday))
                     ? dto.getBreakfastPortion().get()
                     : dto.getDinnerPortion().get();
               return getMealResponse(id, dto, name, portion);

            default:
               LOG.error("found more than 3 meals assigned?");
               return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
         }
      }

      LOG.info("Pupil with id: " + id + " was NOT found in ADB");
      return new ResponseEntity<>(NOT_FOUND);
   }


   private ResponseEntity<ClientPupilDto> getMealResponse(long id, AdbPupilDto dto, String name, Portion portion) {

      if (pupilService.canHaveMeal(id, new Date(), portion.getType())) {
         mealService.saveMealEvent(new MealEvent(id, name, new Date(), portion.getPrice(), portion.getType()));
         LOG.info("OK - Pupil '" + name + "' (id " + id + ") is getting " + portion.getType().name() + " for " + portion.getPrice() + " units of money");
         return new ResponseEntity<>(new ClientPupilDto(id, name, portion, dto.getGrade().orElse("")), ACCEPTED);

      } else {
         LOG.info("REJECT - Pupil '" + name + "' (id " + id + ") already had his meal");
         return new ResponseEntity<>(new ClientPupilDto(id, name, portion, dto.getGrade().orElse("")), ALREADY_REPORTED);
      }
   }
}
