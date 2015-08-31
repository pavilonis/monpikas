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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.nanoTime;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class PupilService {

   private static final Logger LOG = getLogger(PupilService.class.getSimpleName());

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilRepository pupilsRepository;

   @Autowired
   private MealEventLogRepository eventsRepository;

   /**
    * @return List of Pupils from ADB merged with local Pupil information
    */
   public List<PupilDto> getMergedList() {
      List<PupilDto> adbPupils = getAdbPupilDtos();
      List<Pupil> pupils = getPupilInfos();

      long start = nanoTime();
      adbPupils.stream()
            .forEach(adb -> pupils.stream().parallel()
                  .filter(pupil -> adb.getCardId() == pupil.getCardId())
                  .findAny()
                  .ifPresent(pupil -> {
                     adb.setMeals(pupil.getMeals());
                     adb.setComment(ofNullable(pupil.getComment()));
                     adb.setPupilType(pupil.getType());
                  }));
      LOG.info("merged AdbPupils with PupilInfo in " + durationFrom(start) + " Returning " + adbPupils.size() + " adbDto's");

      return adbPupils;
   }

   public List<PupilDto> getMergedWithPortions() {
      return getMergedList().stream()
            .filter(dto -> !isEmpty(dto.getMeals()))
            .collect(Collectors.toList());
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

      //getting information about pupil
      dto.ifPresent(d -> pupilsRepository.findByCardId(cardId)
            .ifPresent(i -> {
               d.setMeals(i.getMeals());
               d.setPupilType(i.getType());
               d.setComment(ofNullable(i.getComment()));
            }));
      return dto;
   }

   public Optional<Pupil> infoByCardId(long cardId) {
      return pupilsRepository.findByCardId(cardId);
   }

   public void saveOrUpdate(Pupil info) {
      pupilsRepository.saveAndFlush(info);
   }

   public boolean canHaveMeal(long cardId, Date day, MealType type) {
      long mealsThatDay = eventsRepository.numOfMealEvents(cardId, beginning(day), end(day), type);
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(long cardId, MealType type) {
      Optional<Pupil> pupil = pupilsRepository.findByCardId(cardId);

      return pupil.orElseThrow(IllegalArgumentException::new)
            .getMeals().stream()
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