package lt.pavilonis.monpikas.key;

import lt.pavilonis.monpikas.user.User;
import lt.pavilonis.monpikas.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;
import static lt.pavilonis.monpikas.common.util.TimeUtils.duration;

@RequestMapping("/rest/keys")
@RestController
public class KeyRestController {

   private static final Logger LOGGER = LoggerFactory.getLogger(KeyRestController.class);
   private final KeyRepository keyRepository;
   private final UserRepository userRepository;

   public KeyRestController(KeyRepository keyRepository, UserRepository userRepository) {
      this.keyRepository = keyRepository;
      this.userRepository = userRepository;
   }

   @PostMapping("/{scannerId}/{keyNumber}/{cardCode}")
   public ResponseEntity<Key> assignKey(@PathVariable long scannerId, @PathVariable String cardCode,
                                        @PathVariable int keyNumber) {

      User user = userRepository.load(cardCode, false);
      if (user == null) {
         LOGGER.warn("Key assign fail - user not found [cardCode={}]", cardCode);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      List<Key> alreadyAssignedKeys = keyRepository.loadActive(scannerId, null, null, keyNumber);
      if (!alreadyAssignedKeys.isEmpty()) {
         LOGGER.warn("Key assign fail - key already assigned [scannerId={}, keyNumber={}]", scannerId, keyNumber);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
      }

      Key savedKey = keyRepository.assign(scannerId, user.getId(), keyNumber);
      LOGGER.info("Key assigned [keyNumber={}, scannerId={}, cardCode={}, user={}]",
            savedKey.getKeyNumber(), scannerId, cardCode, savedKey.getUser().getName());
      return ResponseEntity.ok().body(savedKey);
   }


   @DeleteMapping("/{scannerId}/{keyNumber}")
   public ResponseEntity<Key> unAssignKey(@PathVariable long scannerId, @PathVariable int keyNumber) {

      var opStart = now();
      Key savedKey = keyRepository.unAssign(scannerId, keyNumber);

      LOGGER.info("Key unassigned [scannerId={}, key={}, t={}]", scannerId, keyNumber, duration(opStart));
      return ResponseEntity.ok().body(savedKey);
   }


   @GetMapping
   public ResponseEntity<List<Key>> loadActive(@RequestParam(required = false) Long scannerId,
                                               @RequestParam(required = false) String cardCode,
                                               @RequestParam(required = false) String keyNumber) {
      Integer keyNum = parseInteger(keyNumber);
      List<Key> keysTaken = keyRepository.loadActive(scannerId, null, cardCode, keyNum);

      return ResponseEntity.ok().body(keysTaken);
   }

   @GetMapping("/log")
   public ResponseEntity<List<Key>> loadLog(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                            @RequestParam LocalDate periodStart,
                                            @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            @RequestParam LocalDate periodEnd,
                                            @RequestParam(required = false) Long scannerId,
                                            @RequestParam(required = false) String keyNumber,
                                            @RequestParam(required = false) String keyAction,
                                            @RequestParam(required = false) String nameLike) {
      var opStart = now();
      Integer keyNum = parseInteger(keyNumber);
      KeyAction action = parseKeyAction(keyAction);

      List<Key> result = keyRepository.loadLog(periodStart, periodEnd, scannerId, keyNum, action, nameLike);
      LOGGER.info("Returning keyLogs [number={}, t={}]", result.size(), duration(opStart));
      return ResponseEntity.ok().body(result);
   }

   private KeyAction parseKeyAction(String keyAction) {
      if (!StringUtils.hasText(keyAction)) {
         return null;
      }
      try {
         return KeyAction.valueOf(keyAction);

      } catch (IllegalArgumentException e) {
         LOGGER.error("Could not parse enum", e);
         return null;
      }
   }

   private Integer parseInteger(String keyNumber) {
      if (!StringUtils.hasText(keyNumber)) {
         return null;
      }
      try {
         return Integer.parseInt(keyNumber);
      } catch (NumberFormatException e) {
         LOGGER.error("Could not parse number", e);
         return null;
      }
   }
}
