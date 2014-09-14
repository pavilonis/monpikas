package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.PupilDto;
import lt.pavilonis.monpikas.server.domain.PupilInfo;

import java.util.List;

public interface PupilService {

   List<PupilDto> getOriginalList();

   PupilDto getByCardId(long cardId);

   void saveOrUpdate(PupilInfo info);
}
