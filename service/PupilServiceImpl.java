package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.repositories.MealEventRepository;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PupilServiceImpl implements PupilService {

   private static final Logger LOG = Logger.getLogger(PupilServiceImpl.class.getName());

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilInfoRepository pupilRepository;

   @Autowired
   private MealEventRepository mealRepository;

   @Autowired
   private MealService mealService;

   public List<AdbPupilDto> getMergedList() {
      long start = System.nanoTime();
      List<PupilInfo> pupilInfos = pupilRepository.findAll();
      long finish = System.nanoTime();
      LOG.info("got ALL PupilInfo in " + (finish - start) / 1000000 + " millis");

      start = System.nanoTime();
      List<AdbPupilDto> pupils = dao.getAllAdbPupils();
      finish = System.nanoTime();
      LOG.info("got All AdbPupils in " + (finish - start) / 1000000 + " millis");

      start = System.nanoTime();
      pupils.forEach(
            pupil -> pupilInfos.forEach(
                  pupilInfo -> {
                     if (pupil.getCardId() == pupilInfo.getCardId()) {
                        pupil.setDinnerPermitted(pupilInfo.isDinnerPermitted());
                        pupil.setBreakfastPermitted(pupilInfo.isBreakfastPermitted());
                        pupil.setComment(pupilInfo.getComment());
                     }
                  }
            )
      );
      finish = System.nanoTime();
      LOG.info("merged AdbPupils with PupilInfo in " + (finish - start) / 1000000 + " millis");
      return pupils;
   }

   public AdbPupilDto getByCardId(long cardId) {
      AdbPupilDto dto = dao.getAdbPupil(cardId);
      if (dto != null) {
         PupilInfo info = pupilRepository.findByCardId(cardId);  //getting dinner information about pupil
         dto.setDinnerPermitted(info != null && info.isDinnerPermitted());
         dto.setComment(info == null ? "" : info.getComment());
      }
      return dto;
   }

   @Override
   public void saveOrUpdate(PupilInfo info) {
      pupilRepository.save(info);
   }

   @Override
   public boolean hadDinnerToday(long cardId) {
      Date lastDinner = mealRepository.lastMealEventDate(cardId);
      return lastDinner != null && mealService.sameDay(lastDinner, new Date());
   }

   @Override
   public boolean reachedTodaysMealLimit(AdbPupilDto dto) {
      Long todaysMeals = mealRepository.numOfTodaysMealEventsByCardId(dto.getCardId());
      return todaysMeals == null || todaysMeals < dto.mealsPermitted();
   }
}
