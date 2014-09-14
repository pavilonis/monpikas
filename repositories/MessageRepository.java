package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}