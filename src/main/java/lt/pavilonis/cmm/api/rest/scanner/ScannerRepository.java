package lt.pavilonis.cmm.api.rest.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class ScannerRepository {

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   public Optional<Scanner> load(Long id) {
      List<Scanner> result = query(id);
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   public List<Scanner> loadAll() {
      return query(null);
   }

   private List<Scanner> query(Long id) {
      HashMap<String, Object> args = new HashMap<>();
      args.put("id", id);

      return jdbcSalto.query(
            "SELECT id, name FROM mm_Scanner WHERE :id IS NULL OR :id = id",
            args,
            (rs, i) -> new Scanner(rs.getLong(1), rs.getString(2))
      );
   }
}
