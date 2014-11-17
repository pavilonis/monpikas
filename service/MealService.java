package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.repositories.MealEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class MealService {

   @Autowired
   private MealEventRepository mealRepository;

   public List<MealEvent> getDinnerEventList() {
      Calendar minDate = Calendar.getInstance();
      minDate.add(Calendar.DATE, -30);
      return mealRepository.findAfter(minDate.getTime());
   }

   public boolean sameDay(Date date1, Date date2) {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      cal1.setTime(date1);
      cal2.setTime(date2);
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
   }

   public Optional<Date> lastMealEvent(long cardId) {
      return ofNullable(mealRepository.lastMealEventDate(cardId));
   }

   public void saveMealEvent(MealEvent m) {
      mealRepository.save(m);
   }

   public void deleteMealEvent(long id) {
      mealRepository.delete(id);
   }
}
