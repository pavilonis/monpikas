package lt.pavilonis.cmm.school.classroom;

import lt.pavilonis.cmm.api.rest.classroom.ClassroomOccupancy;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import lt.pavilonis.cmm.common.EntityRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ClassroomListRepository implements EntityRepository<ClassroomOccupancy, Void, ClassroomFilter> {

   @Autowired
   private ClassroomRepository repository;

   @Override
   public ClassroomOccupancy saveOrUpdate(ClassroomOccupancy entity) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public List<ClassroomOccupancy> load() {
      throw new NotImplementedException("Not needed yet");
   }

   @Override
   public List<ClassroomOccupancy> load(ClassroomFilter filter) {
      if (!filter.isHistoryMode()) {
         // null param => all levels
         return repository.loadActive(null);
      }

      return repository.load(
            filter.getPeriodStart() == null ? null : filter.getPeriodStart().atTime(LocalTime.MIN),
            filter.getPeriodEnd() == null ? null : filter.getPeriodEnd().atTime(LocalTime.MAX),
            filter.getText()
      );
   }

   @Override
   public Optional<ClassroomOccupancy> find(Void aVoid) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public void delete(Void aVoid) {
      throw new NotImplementedException("not needed");
   }

   @Override
   public Class<ClassroomOccupancy> entityClass() {
      return ClassroomOccupancy.class;
   }
}
