package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DinnerEventRepository extends JpaRepository<DinnerEvent, Long> {

   @Query("select max(e.date) from DinnerEvent e where e.pupilInfo = :info")
   DinnerEvent findLast(@Param("info")PupilInfo info);
}