package lt.pavilonis.cmm.api.rest.scanlog;

import lt.pavilonis.cmm.api.rest.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/scanlog")
@RestController
public class ScanLogController {
   private static final Logger LOG = LoggerFactory.getLogger(ScanLogController.class.getSimpleName());

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ScanLogRepository scanLogRepository;

   @PostMapping("/{scannerId}/{cardCode}")
   public ResponseEntity<ScanLog> writeLog(@PathVariable long scannerId, @PathVariable String cardCode) {

      if (!userRepository.exists(cardCode)) {
         LOG.warn("ScanLog skipped [reason={}, cardCode={}]", "user not found ", cardCode);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      ScanLog result = scanLogRepository.save(scannerId, cardCode);
      return ResponseEntity.ok().body(result);
   }
}
