package lt.pavilonis.monpikas.server.repositories;

import lt.pavilonis.monpikas.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}