package lt.pavilonis.cmm.api.rest.scanlog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/scanlog")
@RestController
public class ScanLogRestController {

   private final ScanLogRepository scanLogRepository;

   public ScanLogRestController(ScanLogRepository scanLogRepository) {
      this.scanLogRepository = scanLogRepository;
   }

   @PostMapping("/{scannerId}/{cardCode}")
   public ResponseEntity<ScanLog> writeLog(@PathVariable long scannerId,
                                           @PathVariable String cardCode) {

      ScanLog result = scanLogRepository.store(scannerId, cardCode);

      return result == null
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            : ResponseEntity.ok().body(result);
   }
}
