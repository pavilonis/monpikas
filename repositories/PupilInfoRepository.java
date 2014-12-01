package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.PupilInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PupilInfoRepository extends JpaRepository<PupilInfo, Long> {

   PupilInfo findByCardId(long id);

   List<PupilInfo> findByDinnerPortionIsNotNullOrBreakfastPortionIsNotNull();

   @Query("select i from PupilInfo i, Portion p where i.breakfastPortion = p or i.dinnerPortion = p and p.id = :id")
   List<PupilInfo> findFirstByPortionId(@Param("id") long id);
}