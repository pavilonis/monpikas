package lt.pavilonis.monpikas.server.controllers;

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

import java.util.Optional;

import static java.time.LocalTime.now;
import static java.time.LocalTime.parse;
import static org.slf4j.LoggerFactory.getLogger;

@RequestMapping("rest")
@RestController
public class MealRestController {
   private static final Logger LOG = getLogger(MealRestController.class);

   @Value("${time.breakfastDinnerBorder}")
   private String breakfastDinnerBorder;

   @Autowired
   PupilService pupilService;

   @Autowired
   MealService mealService;

   @ResponseBody
   @RequestMapping("asdf")
   public void asdf() {
      if (now().isBefore(parse(breakfastDinnerBorder))) {
         System.out.println("mourning");
      } else {
         System.out.println("eventing");
      }
   }


   @ResponseBody
   @RequestMapping("mealRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {
//      if (now().isBefore(parse(breakfastDinnerBorder))) {
//         System.out.println("mourning");
//      } else {
//         System.out.println("eventing");
//      }
      LOG.info("Pupil with id: " + id + " requested a meal");
      Optional<AdbPupilDto> adbDto = pupilService.getByCardId(id);

      adbDto.ifPresent(d -> {
         String fullName = d.getFirstName() + " " + d.getLastName();
//
//         LocalTime.now()
//         d.getBreakfastPortion().ifPresent(p);
//         if (!adbDto.getDinnerPortion().isPresent()) {
//            LOG.info("Pupil with id: " + id + " has NO PERMISSION to have a meal");
//            return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, false, false), HttpStatus.OK);
//
//         } else if (pupilService.reachedMealLimit(adbDto)) {
//            LOG.info("Pupil with id: " + id + " reached meal limit today");
//            return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, true, true), HttpStatus.OK);
//
//         } else {
//            LOG.info("Pupil with id: " + id + " is getting the meal");
//            mealService.saveMealEvent(adbDto.getCardId(), fullName);
//            return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, true, false), HttpStatus.OK);
//         }
      });

      LOG.info("Pupil with id: " + id + " was NOT found in ADB");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   }
}
