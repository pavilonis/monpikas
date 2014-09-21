package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.DinnerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface DinnerEventRepository extends JpaRepository<DinnerEvent, Long> {

   @Query("select max(e.date) from DinnerEvent e where e.cardId = :cardId")
   Date lastDinnerEventDate(@Param("cardId") long cardId);
}