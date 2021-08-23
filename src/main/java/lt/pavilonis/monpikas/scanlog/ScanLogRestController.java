package lt.pavilonis.monpikas.scanlog;

import lt.pavilonis.monpikas.scanlog.brief.ScanLogBrief;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rest/scanlog")
@RestController
public class ScanLogRestController {

   // This constant should not change as it is used on client side
   private static final String UNKNOWN_USER = "Unknown user";
   private final ScanLogRepository scanLogRepository;

   public ScanLogRestController(ScanLogRepository scanLogRepository) {
      this.scanLogRepository = scanLogRepository;
   }

   @PostMapping("/{scannerId}/{cardCode}")
   public ResponseEntity<?> writeLog(@PathVariable long scannerId, @PathVariable String cardCode) {

      ScanLog result = scanLogRepository.store(scannerId, cardCode);

      return result == null
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(UNKNOWN_USER + " " + cardCode)
            : ResponseEntity.ok().body(result);
   }

   @GetMapping("/lastseen")
   public List<ScanLogBrief> loadBriefLog(@RequestParam(required = false) String text) {
      return scanLogRepository.loadLastUserLocations(text);
   }
}
