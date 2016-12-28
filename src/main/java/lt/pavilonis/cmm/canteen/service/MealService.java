package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
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
   private MealEventLogRepository mealRepository;

   public List<MealEventLog> getDinnerEventList() {
      Calendar from = Calendar.getInstance();
      from.add(Calendar.DATE, -100); // 100 days back
      return mealRepository.loadAfter(from.getTime());
   }

   public boolean sameDay(Date date1, Date date2) {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      cal1.setTime(date1);
      cal2.setTime(date2);
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
   }

   public Optional<Date> lastMealEvent(String cardCode) {
      return ofNullable(mealRepository.lastMealEventDate(cardCode));
   }
}
