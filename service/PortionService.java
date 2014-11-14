package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.repositories.PortionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortionService {

   @Autowired
   private PortionRepository portionRepository;

   public List<Portion> getAll() {
      return portionRepository.findAll();
   }

   public void delete(long id) {
      portionRepository.delete(id);
   }

   public void save(Portion portion) {
      portionRepository.save(portion);
   }
}
