package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Portion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortionRepository extends JpaRepository<Portion, Long> {
}
