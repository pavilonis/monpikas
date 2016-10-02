package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.PupilRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.nanoTime;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class PupilService {

   private static final Logger LOG = LoggerFactory.getLogger(PupilService.class.getSimpleName());

   @Autowired
   private PupilRepository pupilsRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   public List<Pupil> loadPupilsWithAssignedPortions() {
      return pupilsRepository.loadAll().stream()
            .filter(dto -> !isEmpty(dto.meals))
            .collect(Collectors.toList());
   }

   public Optional<Pupil> getByCardCode(String cardCode) {
      return pupilsRepository.getAdbPupil(cardCode, infoByCardCode(cardCode));
   }

   public Optional<Pupil> infoByCardCode(String cardCode) {
      return pupilsRepository.findByCardCode(cardCode);
   }

   public boolean canHaveMeal(String cardCode, Date day, MealType type) {
      int mealsThatDay = eventsRepository.numOfMealEvents(cardCode, beginning(day), end(day), type);
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(String cardCode, MealType type) {
      Optional<Pupil> pupil = pupilsRepository.findByCardCode(cardCode);

      return pupil.orElseThrow(IllegalArgumentException::new)
            .meals.stream()
            .anyMatch(portion -> portion.getType() == type);
   }

   private Date beginning(Date date) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
   }

   private Date end(Date date) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      c.set(Calendar.HOUR_OF_DAY, 23);
      c.set(Calendar.MINUTE, 59);
      c.set(Calendar.SECOND, 59);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
   }

   private static String durationFrom(long start) {
      return (nanoTime() - start) / 1_000_000 + " ms.";
   }
}