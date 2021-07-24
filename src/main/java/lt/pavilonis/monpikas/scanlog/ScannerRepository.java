package lt.pavilonis.monpikas.scanlog;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ScannerRepository {

   private final NamedParameterJdbcTemplate jdbc;

   public ScannerRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<Scanner> loadAll() {
      return jdbc.query(
            "SELECT id, name FROM Scanner",
            Map.of(),
            (rs, i) -> new Scanner(rs.getLong("id"), rs.getString("name"))
      );
   }
}
