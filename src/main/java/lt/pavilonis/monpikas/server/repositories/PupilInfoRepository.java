package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.PupilInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PupilInfoRepository extends JpaRepository<PupilInfo, Long> {
   PupilInfo findByCardId(long id);
}