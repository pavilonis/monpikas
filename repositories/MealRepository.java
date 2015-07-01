package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MealRepository extends JpaRepository<Meal, Long> {
   Collection<Meal> deleteByIdIn(Collection<Long> idList);
}
