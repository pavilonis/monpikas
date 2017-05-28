package lt.pavilonis.cmm.api.rest.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/scanner")
@RestController
public class ScannerController {

   @Autowired
   private ScannerRepository scannerRepository;

   @GetMapping("/{id}")
   public Scanner load(@PathVariable long id) {

      return scannerRepository.load(id)
            .orElse(null);
   }

   @GetMapping
   public List<Scanner> loadAll() {
      return scannerRepository.loadAll();
   }
}
