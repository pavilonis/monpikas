package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.common.util.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Stream;

@Repository
public class TechnologicalCardRepository implements EntityRepository<TechnologicalCard, Long, IdNameFilter> {
   private static final Logger LOG = LoggerFactory.getLogger(TechnologicalCardRepository.class);
   private static final RowMapper<TechnologicalCard> MAPPER = new TechnologicalCardMapper();
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM Dish d " +
         "  JOIN DishGroup dg ON dg.id = d.dishGroup_id " +
         "WHERE (:id IS NULL OR d.id = :id) AND (:name IS NULL OR d.name LIKE :name) ";

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public TechnologicalCard saveOrUpdate(TechnologicalCard entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());
      args.put("dishGroupId", entity.getDishGroup().getId());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private TechnologicalCard update(Map<String, ?> args) {
      jdbc.update(
            "UPDATE Dish SET name = :name, dishGroup_id = :dishGroupId WHERE id = :id",
            args
      );
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private TechnologicalCard create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update("" +
                  "INSERT INTO Dish (name, dishGroup_id) " +
                  "VALUES (:name, :dishGroupId)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }


   @Override
   public List<TechnologicalCard> load(IdNameFilter filter) {
      List<TechnologicalCard> result = jdbc.query(
            "SELECT d.id, d.name, dg.id, dg.name " + FROM_WHERE_BLOCK + "ORDER BY d.name",
            composeArgs(filter),
            MAPPER
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]",
            result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<TechnologicalCard> find(Long id) {
      List<TechnologicalCard> result = load(new IdNameFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM Dish WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechnologicalCard> entityClass() {
      return TechnologicalCard.class;
   }

   @Override
   public Optional<BackEndDataProvider<TechnologicalCard, IdNameFilter>> lazyDataProvider(IdNameFilter filter) {
      BackEndDataProvider<TechnologicalCard, IdNameFilter> provider = new AbstractBackEndDataProvider<TechnologicalCard, IdNameFilter>() {
         @Override
         protected Stream<TechnologicalCard> fetchFromBackEnd(Query<TechnologicalCard, IdNameFilter> query) {
            IdNameFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return load(updatedFilter).stream();
         }

         @Override
         protected int sizeInBackEnd(Query<TechnologicalCard, IdNameFilter> query) {
            IdNameFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return jdbc.queryForObject(
                  "SELECT COUNT(*) " + FROM_WHERE_BLOCK,
                  composeArgs(updatedFilter),
                  Integer.class
            );
         }
      };
      return Optional.of(provider);
   }

   private Map<String, Object> composeArgs(IdNameFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("name", QueryUtils.likeArg(filter.getName()));
      args.put("offset", filter.getOffSet());
      args.put("limit", filter.getLimit());
      return args;
   }

   //TODO
//   @Override
//   public List<DishItemRecord> items(long dishId) {
//      return dsl().selectFrom(DISHITEM)
//            .where(DISHITEM.DISHID.eq(dishId))
//            .fetch();
//   }
//
//   @Override
//   public BigDecimal totalWeight(long dishId) {
//      return dsl().select(coalesce(sum(DISHITEM.OUTPUTWEIGHT), 0).coerce(BigDecimal.class))
//            .from(DISHITEM)
//            .where(DISHITEM.DISHID.eq(dishId))
//            .fetchOne()
//            .value1();
//   }
//
//   @Override
//   public int kcal(long dishId) {
//      return dsl().select(coalesce(sum(PRODUCTGROUP.KCAL100.mul(DISHITEM.OUTPUTWEIGHT).div(100)), 0).coerce(int.class))
//            .from(DISHITEM)
//            .join(PRODUCTGROUP).on(DISHITEM.PRODUCTGROUPID.eq(PRODUCTGROUP.ID)).and(DISHITEM.DISHID.eq(dishId))
//            .fetchOne()
//            .value1();
//   }
}
