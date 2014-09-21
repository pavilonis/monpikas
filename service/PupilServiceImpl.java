package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.domain.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.repositories.DinnerEventRepository;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

   public List<AdbPupilDto> getOriginalList() {
      long start = System.nanoTime();
      List<PupilInfo> pupilInfos = pupilRepo.findAll();
      long finish = System.nanoTime();
      System.out.println("got ALL PupilInfo in " + (finish - start) / 1000000 + " milis");

      start = System.nanoTime();
      List<AdbPupilDto> pupils = dao.getAllAdbPupils();
      finish = System.nanoTime();
      System.out.println("got All AdbPupils in " + (finish - start) / 1000000 + " milis");

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
      System.out.println("merged AdbPupils with PupilInfo in " + (finish - start) / 1000000 + " milis");
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
   public boolean hadDinnerToday(PupilInfo info) {
      DinnerEvent lastEvent = dinnerRepo.findLast(info);
      if (lastEvent == null) {
         return false;
      } else {
         Calendar cal1 = Calendar.getInstance();
         Calendar cal2 = Calendar.getInstance();
         cal1.setTime(lastEvent.getDate());
         cal2.setTime(new Date());
         return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
      }
   }

   @Override
   public void saveDinnerEvent(PupilInfo info) {
      dinnerRepo.save(new DinnerEvent(info, new Date()));
   }
}
