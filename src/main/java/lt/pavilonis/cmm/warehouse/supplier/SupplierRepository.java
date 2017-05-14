package lt.pavilonis.cmm.warehouse.supplier;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SupplierRepository implements EntityRepository<Supplier, Long, IdTextFilter> {

   private static final RowMapper<Supplier> MAPPER =
         (rs, i) -> new Supplier(rs.getLong(1), rs.getString(2), rs.getString(3));

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public Supplier saveOrUpdate(Supplier entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("code", entity.getCode());
      args.put("name", entity.getName());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private Supplier update(Map<String, ?> args) {
      jdbc.update("UPDATE Supplier SET name = :name, code = :code WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private Supplier create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO Supplier (name, code) VALUES (:name, :code)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<Supplier> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("text", QueryUtils.likeArg(filter.getText()));

      return jdbc.query("" +
                  "SELECT id, code, name " +
                  "FROM Supplier " +
                  "WHERE (:id IS NULL OR id = :id) " +
                  "  AND (:text IS NULL OR name LIKE :text OR code LIKE :text) " +
                  "ORDER BY name",
            args,
            MAPPER
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
      jdbc.update("DELETE FROM Supplier WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<Supplier> entityClass() {
      return Supplier.class;
   }
}
