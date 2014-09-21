package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.AdbDao;
import lt.pavilonis.monpikas.server.domain.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PupilServiceImpl implements PupilService {

   @Autowired
   private AdbDao dao;

   @Autowired
   private PupilInfoRepository pupilRepo;

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
                        pupil.setDinner(pupilInfo.isDinnerPermission());
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
         dto.setDinner(info != null && info.isDinnerPermission());
         dto.setComment(info == null ? "" : info.getComment());
      }
      return dto;
   }

   @Override
   public void saveOrUpdate(PupilInfo info) {
      pupilRepo.save(info);
   }

   @Override
   public boolean hasEatenToday(long cardId) {

      return false;
   }
}
