package lt.pavilonis.monpikas.scanner;

import lombok.AllArgsConstructor;
import lt.pavilonis.monpikas.common.EntityRepository;
import lt.pavilonis.monpikas.common.ui.filter.IdTextFilter;
import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class ScannerRepository implements EntityRepository<Scanner, Long, IdTextFilter> {

   private final NamedParameterJdbcTemplate jdbc;

   @Override
   public Scanner saveOrUpdate(Scanner entity) {
      if (entity.getId() == null) {
         var keyHolder = new GeneratedKeyHolder();
         jdbc.update(
               "INSERT INTO Scanner (name) VALUE (:name)",
               new MapSqlParameterSource("name", entity.getName()),
               keyHolder
         );
         return find(keyHolder.getKey().longValue())
               .orElseThrow();
      }

      jdbc.update("UPDATE Scanner SET name = :name  WHERE id = :id",
            Map.of("name", entity.getName(), "id", entity.getId()));

      return find(entity.getId())
            .orElseThrow();
   }

   @Override
   public List<Scanner> load(IdTextFilter filter) {
      return query(filter);
   }

   @Override
   public List<Scanner> load() {
      return query(new IdTextFilter());
   }

   @Override
   public Optional<Scanner> find(Long id) {
      return query(new IdTextFilter(id))
            .stream()
            .findFirst();
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM Scanner WHERE id = :id", Map.of("id", id));
   }

   @Override
   public Class<Scanner> entityClass() {
      return Scanner.class;
   }

   private List<Scanner> query(IdTextFilter filter) {
      var params = new HashMap<String, Object>();
      params.put("id", filter.getId());
      params.put("text", QueryUtils.likeArg(filter.getText()));

      return jdbc.query(
            "SELECT id, name " +
                  "FROM Scanner " +
                  "WHERE (:id IS NULL OR id = :id) " +
                  "AND (:text IS NULL OR name LIKE :text)",
            params,
            (rs, i) -> new Scanner(rs.getLong("id"), rs.getString("name"))
      );
   }
}
