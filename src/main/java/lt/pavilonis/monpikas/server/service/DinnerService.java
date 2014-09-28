package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.repositories.DinnerEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DinnerService {

   @Autowired
   private DinnerEventRepository dinnerRepo;

   public List<DinnerEvent> getDinnerEventList() {
      return dinnerRepo.findAll();
   }

   public boolean sameDay(Date date1, Date date2) {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      cal1.setTime(date1);
      cal2.setTime(date2);
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
   }

   public Date lastDinner(long cardId) {
      return dinnerRepo.lastDinnerEventDate(cardId);
   }
}
