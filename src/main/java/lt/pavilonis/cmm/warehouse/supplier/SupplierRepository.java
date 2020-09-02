package lt.pavilonis.cmm.warehouse.supplier;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SupplierRepository implements EntityRepository<Supplier, Long, IdTextFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public Supplier saveOrUpdate(Supplier entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("code", entity.getCode());
      args.put("name", entity.getName());
      args.put("dateCreated", new Date());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private Supplier update(Map<String, ?> args) {
      jdbcNamed.update("UPDATE Supplier SET name = :name, code = :code WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private Supplier create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update(
            "INSERT INTO Supplier (name, code, dateCreated) VALUES (:name, :code, :dateCreated)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<Supplier> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<Supplier> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("text", QueryUtils.likeArg(filter.getText()));

      return jdbcNamed.query("" +
                  "SELECT s.id, s.code, s.name " +
                  "FROM Supplier s " +
                  "WHERE (:id IS NULL OR id = :id) " +
                  "  AND (:text IS NULL OR name LIKE :text OR code LIKE :text) " +
                  "ORDER BY name",
            args,
            new SupplierRowMapper()
      );
   }

   @Override
   public Optional<Supplier> find(Long id) {
      List<Supplier> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM Supplier WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<Supplier> entityClass() {
      return Supplier.class;
   }
}
