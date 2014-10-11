package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.dto.ClientPupilDto;
import lt.pavilonis.monpikas.server.service.MealService;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("rest")
@RestController
public class MealRestController {

   private static final Logger LOG = Logger.getLogger(MealRestController.class.getName());

   @Autowired
   PupilService pupilService;

   @Autowired
   MealService mealService;

   @ResponseBody
   @RequestMapping("mealRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {
      LOG.info("Pupil with id: " + id + " requested a meal");
      AdbPupilDto adbDto = pupilService.getByCardId(id);
      boolean notFound = adbDto == null;

      String fullName = notFound ? null : adbDto.getFirstName() + " " + adbDto.getLastName();

      if (notFound) {
         LOG.info("Pupil with id: " + id + " was NOT found in ADB");
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);

      } else if (!adbDto.isDinnerPermitted()) {
         LOG.info("Pupil with id: " + id + " has NO PERMISSION to have a meal");
         return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, false, false), HttpStatus.OK);

      } else if (pupilService.reachedMealLimit(adbDto)) {
         LOG.info("Pupil with id: " + id + " reached meal limit today");
         return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, true, true), HttpStatus.OK);

      } else {
         LOG.info("Pupil with id: " + id + " is getting the meal");
         mealService.saveMealEvent(adbDto.getCardId(), fullName);
         return new ResponseEntity<>(new ClientPupilDto(String.valueOf(id), fullName, true, false), HttpStatus.OK);
      }
   }
}
