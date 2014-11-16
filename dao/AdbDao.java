package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.dto.AdbPupilDto;

import java.util.List;
import java.util.Optional;

public interface AdbDao {

   List<AdbPupilDto> getAllAdbPupils();

   Optional<AdbPupilDto> getAdbPupil(long id);
}
