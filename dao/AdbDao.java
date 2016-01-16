package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.domain.Pupil;
import lt.pavilonis.monpikas.server.dto.PupilDto;

import java.util.List;
import java.util.Optional;

public interface AdbDao {

   List<PupilDto> getAllAdbPupils(List<Pupil> pupilData);

   Optional<PupilDto> getAdbPupil(long id, Optional<Pupil> pupilData);
}
