package lt.pavilonis.monpikas.server.service;

import lt.pavilonis.monpikas.server.dao.PupilDto;

import java.util.List;

public interface PupilService {

   List<PupilDto> getOriginPupils();

   PupilDto getPupil(long cardId);
}
