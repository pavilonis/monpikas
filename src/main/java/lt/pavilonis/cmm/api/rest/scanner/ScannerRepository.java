package lt.pavilonis.cmm.api.rest.scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScannerRepository {

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   public Optional<Scanner> load(long id) {
      List<Scanner> result = query(id);
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   public List<Scanner> loadAll() {
      return query(null);
   }

   private List<Scanner> query(Long id) {
      var sql = "SELECT id, name FROM Scanner";
      RowMapper<Scanner> mapper = (rs, i) -> new Scanner(rs.getLong(1), rs.getString(2));
      return id == null
            ? jdbc.query(sql, Map.of(), mapper)
            : jdbc.query(sql + " WHERE :id = id", Map.of("id", id), mapper);
   }
}
