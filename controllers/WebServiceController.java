package lt.pavilonis.monpikas.server.controllers;

import lt.pavilonis.monpikas.server.domain.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.ClientPupilDto;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceController {

   @Autowired
   PupilService pupilService;

   @ResponseBody
   @RequestMapping("dinnerRequest/{id}")
   public ResponseEntity<ClientPupilDto> dinnerRequest(@PathVariable long id) {
      AdbPupilDto adbDto = pupilService.getByCardId(id);
      if (adbDto == null) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      String fullName = adbDto.getFirstName() + " " + adbDto.getLastName();

      if (!adbDto.isDinner()) {
         return new ResponseEntity<>(new ClientPupilDto(fullName, false, null), HttpStatus.FORBIDDEN);
      }

      if (pupilService.hasEatenToday(adbDto.getCardId()))

      ClientPupilDto clientDto = new ClientPupilDto(
            fullName,
            adbDto.isDinner(),
            )
   }
}
