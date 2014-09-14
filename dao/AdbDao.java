package lt.pavilonis.monpikas.server.dao;

import java.util.List;
import java.util.Set;

public interface AdbDao {

   List<PupilDto> getAllAdbPupils();

   List<PupilDto> getAdbPupilsByCardIds(Set<Long> cardIds);

   PupilDto getAdbPupil(long id);
}
