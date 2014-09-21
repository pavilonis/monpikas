package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.domain.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.ClientPupilDto;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class WebServiceController {

   private static final Logger LOG = Logger.getLogger(WebServiceController.class.getName());

   @Autowired
   PupilService pupilService;

   @Transactional
   @ResponseBody
   @RequestMapping("dinnerRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {
      LOG.info("Pupil with id: " + id + " requested a dinner");
      AdbPupilDto adbDto = pupilService.getByCardId(id);
      boolean notFound = adbDto == null;

      String fullName = notFound ? null : adbDto.getFirstName() + " " + adbDto.getLastName();

      if (notFound) {
         LOG.info("Pupil with id: " + id + " was NOT found in ADB");
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);

      } else if (!adbDto.isDinnerPermitted()) {
         LOG.info("Pupil with id: " + id + " has NO PERMISSION to dinner");
         return new ResponseEntity<>(new ClientPupilDto(fullName, false, null), HttpStatus.FORBIDDEN);

      } else if (pupilService.hadDinnerToday(adbDto.extractPupilInfo())) {
         LOG.info("Pupil with id: " + id + " ALREADY HAD a dinner today");
         return new ResponseEntity<>(new ClientPupilDto(fullName, true, true), HttpStatus.FORBIDDEN);

      } else {
         LOG.info("Pupil with id: " + id + " is getting a dinner");
         pupilService.saveDinnerEvent(adbDto.extractPupilInfo());
         return new ResponseEntity<>(new ClientPupilDto(fullName, true, false), HttpStatus.OK);
      }
   }
}
