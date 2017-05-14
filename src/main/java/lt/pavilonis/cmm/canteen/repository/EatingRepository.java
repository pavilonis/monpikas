package lt.pavilonis.cmm.canteen.repository;

import lt.pavilonis.cmm.canteen.domain.Eating;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static java.util.Objects.isNull;

@Repository
public class EatingRepository implements EntityRepository<Eating, Long, IdTextFilter> {

   private final EatingMapper MAPPER = new EatingMapper();

   @Autowired
   private JdbcTemplate jdbc;

   @Autowired
   private NamedParameterJdbcTemplate namedJdbc;

   @Override
   public List<Eating> load(IdTextFilter filter) {
      return jdbc.query("SELECT e.* FROM Eating e", MAPPER);
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM Eating WHERE id = ?", id);
   }

   @Override
   public Class<Eating> entityClass() {
      return Eating.class;
   }

   public List<Eating> load(Collection ids) {
      return CollectionUtils.isEmpty(ids)
            ? Collections.emptyList()
            : namedJdbc.query("SELECT e.* FROM Eating e WHERE e.id IN (:ids)", singletonMap("ids", ids), MAPPER);
   }

   @Override
   public Optional<Eating> find(Long id) {
      List<Eating> result = jdbc.query("SELECT e.* FROM Eating e WHERE e.id = ?", MAPPER, id);

      return result.isEmpty()
            ? Optional.<Eating>empty()
            : Optional.of(result.get(0));
   }

   @Override
   public Eating saveOrUpdate(Eating eating) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", eating.getId());
      args.put("eatingName", eating.getName());
      args.put("type", eating.getType().name());
      args.put("price", eating.getPrice());
      args.put("startTime", minutes(eating.getStartTime()));
      args.put("endTime", minutes(eating.getEndTime()));

      return isNull(eating.getId())
            ? save(args)
            : update(args);
   }

   protected long minutes(LocalTime localTime) {
      return localTime == null
            ? 0
            : ChronoUnit.MINUTES.between(LocalTime.MIN, localTime);
   }

   private Eating save(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      namedJdbc.update(
            "INSERT INTO Eating (name, type, price, startTime, endTime) " +
                  "VALUES (:eatingName, :type, :price, :startTime, :endTime)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      Set<Long> singleKey = Collections.singleton(keyHolder.getKey().longValue());
      return load(singleKey).get(0);
   }

   private Eating update(Map<String, Object> args) {
      namedJdbc.update("" +
                  "UPDATE " +
                  "  Eating " +
                  "SET " +
                  "  `name` = :eatingName, " +
                  "  `type` = :type, " +
                  "  `price` = :price, " +
                  "  `startTime` = :startTime, " +
                  "  `endTime` = :endTime " +
                  "WHERE id = :id ",
            args
      );
      long eatingId = (long) args.get("id");
      return load(Collections.singleton(eatingId)).get(0);
   }
}
