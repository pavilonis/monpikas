package lt.pavilonis.cmm.api.rest.eating;

import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.EatingType;
import lt.pavilonis.cmm.canteen.domain.PupilRepresentation;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.domain.UserEating;
import lt.pavilonis.cmm.canteen.repository.EatingEventRepository;
import lt.pavilonis.cmm.canteen.service.UserEatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static java.time.LocalTime.now;

@RequestMapping("rest")
@RestController
public class EatingController {

   private static final Logger LOG = LoggerFactory.getLogger(EatingController.class);
   private static final long SCANNER_ID_CANTEEN = 6;

   private final UserEatingService eatingService;
   private final EatingEventRepository eventsRepo;
   private final ScanLogRepository scanLogRepository;

   public EatingController(UserEatingService eatingService, EatingEventRepository eventsRepo,
                           ScanLogRepository scanLogRepository) {

      this.eatingService = eatingService;
      this.eventsRepo = eventsRepo;
      this.scanLogRepository = scanLogRepository;
   }

   @ResponseBody
   @GetMapping("eating/{cardCode}")
   public ResponseEntity<PupilRepresentation> process(@PathVariable String cardCode) {
      LOG.info("Processing STARTING [cardCode={}]", cardCode);

      scanLogRepository.writeScanLog(SCANNER_ID_CANTEEN, cardCode);

      Optional<UserEating> optionalUserEating = eatingService.find(cardCode);


      if (!optionalUserEating.isPresent()) {
         LOG.info("Processing FINISHED: user not found [cardCode={}]", cardCode);
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      UserEating userEating = optionalUserEating.get();
      Set<Eating> eatings = userEating.getEatingData().getEatings();

      if (eatings.isEmpty()) {
         return forbidden(userEating);

      } else if (eatings.size() == 1) {
         return successIfFirstTimePerDay(userEating, eatings.iterator().next());

      } else {
         LocalTime now = now();
         return eatings.stream()
               .filter(m -> m.getStartTime().isBefore(now))
               .filter(m -> m.getEndTime().isAfter(now))
               .map(m -> successIfFirstTimePerDay(userEating, m))
               .findFirst()
               .orElseGet(() -> new ResponseEntity<>(HttpStatus.MULTIPLE_CHOICES)); // TODO bad return status?
      }
   }

   private ResponseEntity<PupilRepresentation> forbidden(UserEating userEating) {
      User user = userEating.getUser();
      LOG.info("Processing FINISHED: no permission [cardCode={}]", user.getCardCode());

      PupilRepresentation response = new PupilRepresentation(
            user.getCardCode(),
            user.getName(),
            null,
            user.getGroup(),
            null,
            user.getBase16photo()
      );
      return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
   }

   private ResponseEntity<PupilRepresentation> successIfFirstTimePerDay(UserEating userEating, Eating eating) {

      User user = userEating.getUser();
      PupilType pupilType = userEating.getEatingData().getType();
      String cardCode = user.getCardCode();
      String name = user.getName();
      EatingType eatingType = eating.getType();

      PupilRepresentation response =
            new PupilRepresentation(cardCode, name, eating, user.getGroup(), pupilType, user.getBase16photo());

      if (!eatingService.canEat(cardCode, new Date(), eatingType)) {
         LOG.info("Processing FINISHED: REJECT - user already had his eating [name={}, cardCode={}]",
               name, cardCode);
         return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
      }

      eventsRepo.saveOrUpdate(new EatingEvent(
            null, cardCode, name, user.getGroup(), new Date(), eating.getPrice(), eatingType, pupilType
      ));

      LOG.info("Processing FINISHED: OK [name={}, cardCode={}, eatingType={}, price={}]",
            name, cardCode, eatingType.name(), eating.getPrice());

      return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
   }
}

