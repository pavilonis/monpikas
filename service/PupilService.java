package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.repositories.MealEventRepository;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PupilService {

   private static final Logger LOG = getLogger(PupilService.class);

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilInfoRepository pupilRepository;

   @Autowired
   private MealEventRepository mealRepository;

   @Autowired
   private MealService mealService;

   /**
    * @return List of Pupils from ADB merged with local Pupil information (PupilInfo)
    */
   public List<AdbPupilDto> getMergedList() {
      return merge(
            getPupilInfos(),
            getAdbPupilDtos(),
            false
      );
   }

   public List<AdbPupilDto> getMergedMealAllowedList() {
      return merge(
            pupilRepository.findByDinnerPermittedTrueOrBreakfastPermittedTrue(),
            getAdbPupilDtos(),
            true
      );
   }

   private List<AdbPupilDto> merge(List<PupilInfo> pupilInfos, List<AdbPupilDto> adbPupils, boolean mealAllowedOnly) {
      long start = System.nanoTime();
      List<AdbPupilDto> result = new ArrayList<>();
      adbPupils.forEach(dto -> {
         pupilInfos.stream()
               .filter(i -> !mealAllowedOnly || (i.isBreakfastPermitted() || i.isDinnerPermitted()))
               .forEach(info -> {
                  if (dto.getCardId() == info.getCardId()) {
                     dto.setDinnerPermitted(info.isDinnerPermitted());
                     dto.setBreakfastPermitted(info.isBreakfastPermitted());
                     dto.setComment(info.getComment());
                  }
               });
         if (!mealAllowedOnly || (dto.isBreakfastPermitted() || dto.isDinnerPermitted())) {
            result.add(dto);
         }

      });
      long finish = System.nanoTime();
      LOG.info("merged AdbPupils with PupilInfo in " + (finish - start) / 1000000 + " millis. Returning " + result.size() + " dto's");
      return result;
   }

   private List<PupilInfo> getPupilInfos() {
      long start = System.nanoTime();
      List<PupilInfo> pupilInfos = pupilRepository.findAll();
      long finish = System.nanoTime();
      LOG.info("got ALL PupilInfo in " + (finish - start) / 1000000 + " millis");
      return pupilInfos;
   }

   private List<AdbPupilDto> getAdbPupilDtos() {
      long start = System.nanoTime();
      List<AdbPupilDto> pupils = dao.getAllAdbPupils();
      long finish = System.nanoTime();
      LOG.info("got All AdbPupils in " + (finish - start) / 1000000 + " millis");
      return pupils;
   }


   public AdbPupilDto getByCardId(long cardId) {
      AdbPupilDto dto = dao.getAdbPupil(cardId);
      if (dto != null) {
         PupilInfo info = pupilRepository.findByCardId(cardId);  //getting dinner information about pupil
         dto.setBreakfastPermitted(info != null && info.isBreakfastPermitted());
         dto.setDinnerPermitted(info != null && info.isDinnerPermitted());
         dto.setComment(info == null ? "" : info.getComment());
      }
      return dto;
   }

   public void saveOrUpdate(PupilInfo info) {
      pupilRepository.save(info);
   }

   public boolean hadDinnerToday(long cardId) {
      Date lastDinner = mealRepository.lastMealEventDate(cardId);
      return lastDinner != null && mealService.sameDay(lastDinner, new Date());
   }

   public boolean reachedMealLimit(AdbPupilDto dto) {
      return reachedMealLimit(dto, null);
   }

   public boolean reachedMealLimit(AdbPupilDto dto, Date date) {
      Long todaysMeals = mealRepository.numOfTodaysMealEventsByCardId(dto.getCardId(), midnight(date));
      return todaysMeals >= dto.mealsPermitted();
   }

   public boolean reachedMealLimit(long cardId, Date date) {
      Long todaysMeals = mealRepository.numOfTodaysMealEventsByCardId(cardId, midnight(date));
      return todaysMeals >= getByCardId(cardId).mealsPermitted();
   }

   private Date midnight(Date date) {
      Calendar c = Calendar.getInstance();
      if (!(date == null)) {
         c.setTime(date);
      }
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      return c.getTime();
   }
}