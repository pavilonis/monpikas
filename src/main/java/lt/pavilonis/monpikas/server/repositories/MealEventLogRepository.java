package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.PupilType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MealEventLogRepository extends JpaRepository<MealEventLog, Long> {


   @Query("select max(e.date) from MealEventLog e where e.cardId = :cardId")
   Date lastMealEventDate(@Param("cardId") long cardId);


   @Query("select m.cardId from MealEventLog m where m.date > CURDATE()")
   List<Long> todaysMealEvents();


   @Query("select count(m.cardId) from MealEventLog m where m.date > :checkDate and m.cardId = :cardId")
   Long numOfTodaysMealEventsByCardId(@Param("cardId") long cardId, @Param("checkDate") Date checkDate);


   @Query("select count(m.cardId) from MealEventLog m" +
         " where (m.date between :start and :end)" +
         " and m.cardId = :cardId" +
         " and m.mealType= :type")
   Long numOfMealEvents(
         @Param("cardId") long cardId,
         @Param("start") Date start,
         @Param("end") Date end,
         @Param("type") MealType type
   );

   @Query("select m from MealEventLog m where m.date > :minDate order by m.date desc")
   List<MealEventLog> findAfter(@Param("minDate") Date minDate);

   List<MealEventLog> findByDateBetweenAndPupilType(Date start, Date end, PupilType pupilType);
}