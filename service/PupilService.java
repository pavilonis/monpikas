package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;
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
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PupilService {

   private static final Logger LOG = getLogger(PupilService.class);

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilInfoRepository infoRepo;

   @Autowired
   private MealEventRepository mealRepo;

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
            infoRepo.findByDinnerPortionIsNotNullOrBreakfastPortionIsNotNull(),
            getAdbPupilDtos(),
            true
      );
   }

   private List<AdbPupilDto> merge(List<PupilInfo> pupilInfos, List<AdbPupilDto> adbPupils, boolean mealAllowedOnly) {
      long start = System.nanoTime();
      List<AdbPupilDto> result = new ArrayList<>();
      adbPupils.forEach(dto -> {
         pupilInfos.stream()
               .filter(i -> !mealAllowedOnly || (i.getBreakfastPortion() != null || i.getDinnerPortion() != null))
               .forEach(info -> {
                  if (dto.getCardId() == info.getCardId()) {
                     dto.setBreakfastPortion(ofNullable(info.getBreakfastPortion()));
                     dto.setDinnerPortion(ofNullable(info.getDinnerPortion()));
                     dto.setGrade(ofNullable(info.getGrade()));
                     dto.setComment(ofNullable(info.getComment()));
                  }
               });
         if (!mealAllowedOnly || (dto.getBreakfastPortion().isPresent() || dto.getDinnerPortion().isPresent())) {
            result.add(dto);
         }

      });
      long finish = System.nanoTime();
      LOG.info("merged AdbPupils with PupilInfo in " + (finish - start) / 1000000 + " millis. Returning " + result.size() + " dto's");
      return result;
   }

   private List<PupilInfo> getPupilInfos() {
      long start = System.nanoTime();
      List<PupilInfo> pupilInfos = infoRepo.findAll();
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


   public Optional<AdbPupilDto> getByCardId(long cardId) {
      Optional<AdbPupilDto> dto = dao.getAdbPupil(cardId);
      dto.ifPresent(d -> {
         Optional<PupilInfo> info = ofNullable(infoRepo.findByCardId(cardId));  //getting information about pupil
         info.ifPresent(i -> {
            d.setBreakfastPortion(ofNullable(i.getBreakfastPortion()));
            d.setDinnerPortion(ofNullable(i.getDinnerPortion()));
            d.setComment(ofNullable(i.getComment()));
         });
      });
      return dto;
   }

   public Optional<PupilInfo> infoByCardId(long cardId) {
      return ofNullable(infoRepo.findByCardId(cardId));
   }

   public void saveOrUpdate(PupilInfo info) {
      infoRepo.saveAndFlush(info);
   }

   public boolean hadMealToday(long cardId) {
      Date lastDinner = mealRepo.lastMealEventDate(cardId);
      return lastDinner != null && mealService.sameDay(lastDinner, new Date());
   }

   public boolean canHaveMeal(long cardId, Date day, PortionType type) {
      long mealsThatDay = mealRepo.numOfMealEvents(cardId, beginning(day), end(day), type.name());
      return mealsThatDay == 0;
   }

   public boolean portionAssigned(long cardId, PortionType type) {
      PupilInfo info = infoRepo.findByCardId(cardId);
      return (type == BREAKFAST && info.getBreakfastPortion() != null) ||
            (type == DINNER && info.getDinnerPortion() != null);
   }

   public int numOfPortionAssigned(AdbPupilDto dto) {
      return (dto.getBreakfastPortion().isPresent() ? 1 : 0)
            + (dto.getDinnerPortion().isPresent() ? 1 : 0);
   }

   public Portion onlyPortionFor(AdbPupilDto dto) {
      return dto.getBreakfastPortion().isPresent()
            ? dto.getBreakfastPortion().get()
            : dto.getDinnerPortion().get();
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
}