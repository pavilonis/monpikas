package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.MealEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MealEventRepository extends JpaRepository<MealEvent, Long> {

   @Query("select max(e.date) from MealEvent e where e.cardId = :cardId")
   Date lastMealEventDate(@Param("cardId") long cardId);

   @Query("select m.cardId from MealEvent m where m.date > CURDATE()")
   List<Long> todaysMealEvents();

   @Query("select count(m.cardId) from MealEvent m where m.date > :checkDate and m.cardId = :cardId")
   Long numOfTodaysMealEventsByCardId(@Param("cardId") long cardId, @Param("checkDate") Date checkDate);

   @Query("select m from MealEvent m where m.date > :minDate")
   List<MealEvent> findAfter(@Param("minDate") Date minDate);
}