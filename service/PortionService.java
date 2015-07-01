package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.Meal;
import lt.pavilonis.monpikas.server.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortionService {

   @Autowired
   private MealRepository mealRepository;

   public List<Meal> getAll() {
      return mealRepository.findAll();
   }

   public void delete(long id) {
      mealRepository.delete(id);
   }

   public void save(Meal meal) {
      mealRepository.save(meal);
   }
}
