package lt.pavilonis.cmm.api.rest.presence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/presence")
public class PresenceTimeController {

   @Autowired
   private PresenceTimeRepository workTimeRepository;

   @GetMapping("/{cardCode}")
   public List<PresenceTime> load(@PathVariable String cardCode) {

      return workTimeRepository.load(cardCode);
   }
}
