package lt.pavilonis.monpikas.server.repositories;


import lt.pavilonis.monpikas.server.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByRole(String role);
}