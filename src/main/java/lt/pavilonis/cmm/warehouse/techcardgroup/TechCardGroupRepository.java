package lt.pavilonis.cmm.warehouse.techcardgroup;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TechCardGroupRepository implements EntityRepository<TechCardGroup, Long, IdTextFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public TechCardGroup saveOrUpdate(TechCardGroup entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private TechCardGroup update(Map<String, ?> args) {
      jdbc.update("UPDATE TechCardGroup SET name = :name WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private TechCardGroup create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO TechCardGroup (name) VALUES (:name)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<TechCardGroup> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("text", QueryUtils.likeArg(filter.getText()));

      return jdbc.query("" +
                  "SELECT tcg.id, tcg.name " +
                  "FROM TechCardGroup tcg " +
                  "WHERE (:id IS NULL OR id = :id) AND (:text IS NULL OR name LIKE :text) " +
                  "ORDER BY name",
            args,
            new TechCardGroupMapper()
      );
   }

   @Override
   public Optional<TechCardGroup> find(Long id) {
      List<TechCardGroup> result = load(new IdTextFilter());
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM TechCardGroup WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechCardGroup> entityClass() {
      return TechCardGroup.class;
   }
}
