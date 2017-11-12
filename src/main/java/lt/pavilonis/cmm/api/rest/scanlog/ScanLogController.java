package lt.pavilonis.cmm.api.rest.scanlog;

import lt.pavilonis.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequestMapping("/rest/scanlog")
@RestController
public class ScanLogController {

   @Autowired
   private ScanLogRepository scanLogRepository;

   @PostMapping("/{scannerId}/{cardCode}")
   public ResponseEntity<ScanLog> writeLog(@PathVariable long scannerId,
                                           @PathVariable String cardCode) {

      ScanLog result = scanLogRepository.saveCheckedAndLoad(scannerId, cardCode);

      return result == null
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
            : ResponseEntity.ok().body(result);
   }

   @GetMapping("/{scannerId}")
   public List<ScanLogBrief> loadBriefLog(@PathVariable long scannerId,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                          @RequestParam(required = false) LocalDate periodStart,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                          @RequestParam(required = false) LocalDate periodEnd,
                                          @RequestParam(required = false) String role,
                                          @RequestParam(required = false) String text) {

      return scanLogRepository.loadBrief(
            new ScanLogBriefFilter(
                  periodStart,
                  periodEnd,
                  text,
                  scannerId,
                  role
            ),
            QueryUtils.argOffset(null),
            QueryUtils.argLimit(null)
      );
   }
}
