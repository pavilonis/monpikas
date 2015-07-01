package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.dto.PupilDto;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.PupilRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.System.nanoTime;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PupilService {

   private static final Logger LOG = getLogger(PupilService.class.getSimpleName());

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilRepository pupilsRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   @Autowired
   private EntityManager entityManager;

   /**
    * @return List of Pupils from ADB merged with local Pupil information
    */
   public List<PupilDto> getMergedList() {
      return merge(
            getPupilInfos(),
            getAdbPupilDtos(),
            true
      );
   }

   public List<PupilDto> getMergedWithPortions() {
      return merge(
            pupilsRepository.findWithPortions(),
            getAdbPupilDtos(),
            false
      );
   }

   private List<PupilDto> merge(List<Pupil> pupils, List<PupilDto> adbPupils, boolean withoutMeals) {
      long start = nanoTime();
      List<PupilDto> result = new ArrayList<>();
      adbPupils.forEach(adbDto -> {
         Optional<Pupil> matchingPupil = pupils.stream()
               .filter(pupil -> (withoutMeals || !pupil.getMeals().isEmpty())
                     && adbDto.getCardId() == pupil.getCardId()).findAny();

         matchingPupil.ifPresent(pupil -> {
            adbDto.setMeals(pupil.getMeals());
            adbDto.setGrade(ofNullable(pupil.getGrade()));
            adbDto.setComment(ofNullable(pupil.getComment()));
            adbDto.setPupilType(pupil.getType());
            result.add(adbDto);
         });
      });

      LOG.info("merged AdbPupils with PupilInfo in " + durationFrom(start) + "Returning " + result.size() + " adbDto's");
      return result;
   }

   private List<Pupil> getPupilInfos() {
      long start = nanoTime();
      List<Pupil> pupils = pupilsRepository.findAll();
      LOG.info(pupils.size() + " Pupils loaded in " + durationFrom(start));
      return pupils;
   }

   private List<PupilDto> getAdbPupilDtos() {
      long start = nanoTime();
      List<PupilDto> pupils = dao.getAllAdbPupils();
      LOG.info(pupils.size() + " AdbPupils loaded in " + durationFrom(start));
      return pupils;
   }

   public Optional<PupilDto> getByCardId(long cardId) {
      Optional<PupilDto> dto = dao.getAdbPupil(cardId);
      dto.ifPresent(d -> {
         Optional<Pupil> info = ofNullable(pupilsRepository.findByCardId(cardId));  //getting information about pupil
         info.ifPresent(i -> {
            d.setMeals(i.getMeals());
            d.setComment(ofNullable(i.getComment()));
            d.setGrade(ofNullable(i.getGrade()));
         });
      });
      return dto;
   }

   public Optional<Pupil> infoByCardId(long cardId) {
      return ofNullable(pupilsRepository.findByCardId(cardId));
   }

   public void saveOrUpdate(Pupil info) {
      pupilsRepository.saveAndFlush(info);
   }

   public boolean canHaveMeal(long cardId, Date day, MealType type) {
      long mealsThatDay = eventsRepository.numOfMealEvents(cardId, beginning(day), end(day), type);
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(long cardId, MealType type) {
      Pupil pupil = pupilsRepository.findByCardId(cardId);
      return pupil.getMeals().stream()
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