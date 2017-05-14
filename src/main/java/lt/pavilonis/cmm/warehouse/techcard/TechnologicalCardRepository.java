package lt.pavilonis.cmm.warehouse.techcard;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
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
public class TechnologicalCardRepository implements EntityRepository<TechnologicalCard, Long, IdTextFilter> {
   private static final Logger LOG = LoggerFactory.getLogger(TechnologicalCardRepository.class);
   private static final RowMapper<TechnologicalCard> MAPPER = new TechnologicalCardMapper();
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM TechnologicalCard tc " +
         "  JOIN TechnologicalCardGroup tcg ON tcg.id = tc.technologicalCardGroup_id " +
         "WHERE (:id IS NULL OR tc.id = :id) AND (:name IS NULL OR tc.name LIKE :name) ";

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public TechnologicalCard saveOrUpdate(TechnologicalCard entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());
      args.put("technologicalCardGroupId", entity.getTechnologicalCardGroup().getId());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private TechnologicalCard update(Map<String, ?> args) {
      jdbc.update("" +
                  "UPDATE TechnologicalCard SET " +
                  "  name = :name," +
                  "  technologicalCardGroup_id = :technologicalCardGroupId " +
                  "WHERE id = :id",
            args
      );
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private TechnologicalCard create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update("" +
                  "INSERT INTO TechnologicalCard (name, technologicalCardGroup_id) " +
                  "VALUES (:name, :technologicalCardGroupId)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }


   @Override
   public List<TechnologicalCard> load(IdTextFilter filter) {
      List<TechnologicalCard> result = jdbc.query(
            "SELECT tc.id, tc.name, tcg.id, tcg.name " + FROM_WHERE_BLOCK + "ORDER BY tc.name",
            composeArgs(filter),
            MAPPER
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]",
            result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<TechnologicalCard> find(Long id) {
      List<TechnologicalCard> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM TechnologicalCard WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechnologicalCard> entityClass() {
      return TechnologicalCard.class;
   }

   @Override
   public Optional<BackEndDataProvider<TechnologicalCard, IdTextFilter>> lazyDataProvider(IdTextFilter filter) {
      BackEndDataProvider<TechnologicalCard, IdTextFilter> provider = new AbstractBackEndDataProvider<TechnologicalCard, IdTextFilter>() {
         @Override
         protected Stream<TechnologicalCard> fetchFromBackEnd(Query<TechnologicalCard, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return load(updatedFilter).stream();
         }

         @Override
         protected int sizeInBackEnd(Query<TechnologicalCard, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
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

   private Map<String, Object> composeArgs(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("name", QueryUtils.likeArg(filter.getText()));
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
