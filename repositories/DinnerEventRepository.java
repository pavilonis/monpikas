package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerEventRepository extends JpaRepository<DinnerEvent, Long> {
   DinnerEvent findByCardId(long id);
}