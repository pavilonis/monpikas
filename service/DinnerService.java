package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.repositories.DinnerEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinnerService {

   @Autowired
   private DinnerEventRepository dinnerRepo;

   public List<DinnerEvent> getDinnerEventList() {
      return dinnerRepo.findAll();
   }
}
