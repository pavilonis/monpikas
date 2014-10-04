package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dto.AdbPupilDto;
import lt.pavilonis.monpikas.server.domain.PupilInfo;

import java.util.List;

public interface PupilService {

   /**
    *
    * @return List of Pupils from ADB merged with local Pupil information (PupilInfo)
    */
   List<AdbPupilDto> getMergedList();

   AdbPupilDto getByCardId(long cardId);

   void saveOrUpdate(PupilInfo info);

   boolean hadDinnerToday(long cardId);

   boolean reachedTodaysMealLimit(AdbPupilDto dto);
}
