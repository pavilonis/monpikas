package lt.pavilonis.cmm.api.rest.key;

import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.util.TimeUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/rest/keys")
@RestController
public class KeyController {

   private static final Logger LOG = LoggerFactory.getLogger(KeyController.class.getSimpleName());

   @Autowired
   private KeyRepository keyRepository;

   @Autowired
   private UserRepository userRepository;

   @PostMapping("/{scannerId}/{keyNumber}/{cardCode}")
   public ResponseEntity<Key> assignKey(@PathVariable long scannerId,
                                        @PathVariable String cardCode,
                                        @PathVariable int keyNumber) {

      if (!userRepository.exists(cardCode)) {
         LOG.warn("Key assign fail - user not found [cardCode={}]", cardCode);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      if (!keyRepository.isAvailable(scannerId, keyNumber)) {
         LOG.warn("Key assign fail - key already assigned [scannerId={}, keyNumber={}]", scannerId, keyNumber);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
      }

      Key savedKey = keyRepository.assign(scannerId, cardCode, keyNumber);
      LOG.info("Key assigned [keyNumber={}, scannerId={}, cardCode={}, user={}]",
            savedKey.getKeyNumber(), scannerId, cardCode, savedKey.getUser().getName());
      return ResponseEntity.ok().body(savedKey);
   }


   @DeleteMapping("/{scannerId}/{keyNumber}")
   public ResponseEntity<Key> unassignKey(@PathVariable long scannerId, @PathVariable int keyNumber) {

      LocalDateTime opStart = LocalDateTime.now();

      Key savedKey = keyRepository.unassign(scannerId, keyNumber);

      LOG.info("Key unassigned [scannerId={}, key={}, duration={}]",
            scannerId, keyNumber, TimeUtils.duration(opStart));

      return ResponseEntity.ok().body(savedKey);
   }


   @GetMapping
   public ResponseEntity<List<Key>> loadActive(@RequestParam(required = false) Long scannerId,
                                               @RequestParam(required = false) String cardCode,
                                               @RequestParam(required = false) String keyNumber) {
      List<Key> keysTaken = keyRepository.loadAssigned(
            scannerId,
            cardCode,
            parseInteger(keyNumber)
      );
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

      LocalDateTime opStart = LocalDateTime.now();
      List<Key> result = keyRepository.loadLog(
            periodStart,
            periodEnd,
            scannerId,
            parseInteger(keyNumber),
            parseKeyAction(keyAction),
            nameLike
      );

      LOG.info("Returning keyLogs [number={}, duration={}]", result.size(), TimeUtils.duration(opStart));
      return ResponseEntity.ok().body(result);
   }

   private KeyAction parseKeyAction(@RequestParam(required = false) String keyAction) {
      return StringUtils.isNotBlank(keyAction) && EnumUtils.isValidEnum(KeyAction.class, keyAction)
            ? KeyAction.valueOf(keyAction)
            : null;
   }

   private Integer parseInteger(@RequestParam(required = false) String keyNumber) {
      return NumberUtils.isDigits(keyNumber)
            ? Integer.parseInt(keyNumber)
            : null;
   }
}
