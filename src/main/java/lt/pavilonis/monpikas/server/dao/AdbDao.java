package lt.pavilonis.monpikas.server.dao;

import lt.pavilonis.monpikas.server.dto.AdbPupilDto;

import java.util.List;
import java.util.Set;

public interface AdbDao {

   List<AdbPupilDto> getAllAdbPupils();

   AdbPupilDto getAdbPupil(long id);

   List<AdbPupilDto>getAdbPupilsByIds(List<Long> cardIds);
}
