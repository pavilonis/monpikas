package lt.pavilonis.cmm.warehouse.dishGroup;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
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
public class DishGroupRepository implements EntityRepository<DishGroup, Long, IdNameFilter> {

   private static final RowMapper<DishGroup> MAPPER =
         (rs, i) -> new DishGroup(rs.getLong(1), rs.getString(2));

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public DishGroup saveOrUpdate(DishGroup entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private DishGroup update(Map<String, ?> args) {
      jdbc.update("UPDATE DishGroup SET name = :name WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private DishGroup create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO DishGroup (name) VALUES (:name)",
            new MapSqlParameterSource(args),
            keyHolder
      );

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<DishGroup> load(IdNameFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("text", QueryUtils.likeArg(filter.getName()));

      return jdbc.query("" +
                  "SELECT id, name " +
                  "FROM DishGroup " +
                  "WHERE (:id IS NULL OR id = :id) AND (:text IS NULL OR name LIKE :text) " +
                  "ORDER BY name",
            args,
            MAPPER
      );
   }

   @Override
   public Optional<DishGroup> find(Long id) {
      List<DishGroup> result = load(new IdNameFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM DishGroup WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<DishGroup> entityClass() {
      return DishGroup.class;
   }

//TODO
//
//   @Override
//   public List<DishRecord> dishes(long groupId) {
//      return dsl().selectFrom(DISH)
//            .where(DISH.DISHGROUPID.eq(groupId))
//            .fetch();
//   }
}
