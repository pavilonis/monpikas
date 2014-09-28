package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.repositories.DinnerEventRepository;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PupilServiceImpl implements PupilService {

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilInfoRepository pupilRepo;

   @Autowired
   private DinnerEventRepository dinnerRepo;

   @Autowired
   private DinnerService dinnerService;

   public List<AdbPupilDto> getOriginalList() {
      long start = System.nanoTime();
      List<PupilInfo> pupilInfos = pupilRepo.findAll();
      long finish = System.nanoTime();
      System.out.println("got ALL PupilInfo in " + (finish - start) / 1000000 + " millis");

      start = System.nanoTime();
      List<AdbPupilDto> pupils = dao.getAllAdbPupils();
      finish = System.nanoTime();
      System.out.println("got All AdbPupils in " + (finish - start) / 1000000 + " millis");

      start = System.nanoTime();
      pupils.forEach(
            pupil -> pupilInfos.forEach(
                  pupilInfo -> {
                     if (pupil.getCardId() == pupilInfo.getCardId()) {
                        pupil.setDinnerPermitted(pupilInfo.isDinnerPermitted());
                        pupil.setComment(pupilInfo.getComment());
                     }
                  }
            )
      );
      finish = System.nanoTime();
      System.out.println("merged AdbPupils with PupilInfo in " + (finish - start) / 1000000 + " millis");
      return pupils;
   }

   public AdbPupilDto getByCardId(long cardId) {
      AdbPupilDto dto = dao.getAdbPupil(cardId);
      if (dto != null) {
         PupilInfo info = pupilRepo.findByCardId(cardId);  //getting dinner information about pupil
         dto.setDinnerPermitted(info != null && info.isDinnerPermitted());
         dto.setComment(info == null ? "" : info.getComment());
      }
      return dto;
   }

   @Override
   public void saveOrUpdate(PupilInfo info) {
      pupilRepo.save(info);
   }

   @Override
   public boolean hadDinnerToday(long cardId) {
      Date lastDinner = dinnerRepo.lastDinnerEventDate(cardId);
      return lastDinner != null && dinnerService.sameDay(lastDinner, new Date());
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean hadDinnerTodayCached(long cardId) {
      List<Long> todaysDinnerEventsIds = dinnerRepo.todaysDinnerEvents();
      return todaysDinnerEventsIds.contains(cardId);
   }

   @Override
   public void saveDinnerEvent(long cardId, String name) {
      dinnerRepo.save(new DinnerEvent(cardId, name, new Date()));
   }
}
