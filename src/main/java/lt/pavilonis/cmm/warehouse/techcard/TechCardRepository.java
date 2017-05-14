package lt.pavilonis.cmm.warehouse.techcard;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.BackEndDataProvider;
import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class TechCardRepository implements EntityRepository<TechCard, Long, IdTextFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(TechCardRepository.class);
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM TechCard tc " +
         "  JOIN TechCardGroup tcg ON tcg.id = tc.techCardGroup_id " +
         "  LEFT JOIN TechCardProduct tcp ON tcp.techCard_id = tc.id " +
         "  LEFT JOIN ProductGroup pg ON pg.id = tcp.productGroup_id " +
         "WHERE (:id IS NULL OR tc.id = :id) AND (:name IS NULL OR tc.name LIKE :name) ";

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Transactional
   @Override
   public TechCard saveOrUpdate(TechCard card) {

      Map<String, Object> args = new HashMap<>();
      args.put(ID, card.getId());
      args.put("name", card.getName());
      args.put("groupId", card.getGroup().getId());

      return card.getId() == null
            ? create(card, args)
            : update(card, args);
   }

   private TechCard update(TechCard card, Map<String, Object> args) {

      jdbc.update("UPDATE TechCard SET name = :name, techCardGroup_id = :groupId WHERE id = :id", args);
      jdbc.update("DELETE FROM TechCardProduct WHERE techCard_id = :id", args);
      saveTechCardProducts(card.getId(), card.getProductGroupOutputWeight());

      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private void saveTechCardProducts(long cardId, Map<ProductGroup, BigDecimal> outputWeights) {

      String query = "INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight) " +
            "VALUES (:cardId, :productGroupId, :outputWeight)";

      outputWeights.forEach((productGroup, outputWeight) -> jdbc.update(query, ImmutableMap.of(
            "cardId", cardId,
            "productGroupId", productGroup.getId(),
            "outputWeight", outputWeight
      )));
   }

   private TechCard create(TechCard card, Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      String query = "INSERT INTO TechCard (name, techCardGroup_id) VALUES (:name, :groupId)";

      jdbc.update(query, new MapSqlParameterSource(args), keyHolder);
      saveTechCardProducts(keyHolder.getKey().longValue(), card.getProductGroupOutputWeight());

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }


   @Override
   public List<TechCard> load(IdTextFilter filter) {
      List<TechCard> result = jdbc.query("" +
                  "SELECT " +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name," +
                  "  tcp.outputWeight," +
                  "  pg.id, pg.name " +
                  FROM_WHERE_BLOCK +
                  "ORDER BY tc.name",
            composeArgs(filter),
            new TechCardResultSetExtractor()
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]",
            result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<TechCard> find(Long id) {
      List<TechCard> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM TechCard WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechCard> entityClass() {
      return TechCard.class;
   }

   @Override
   public Optional<BackEndDataProvider<TechCard, IdTextFilter>> lazyDataProvider(IdTextFilter filter) {
      BackEndDataProvider<TechCard, IdTextFilter> provider = new AbstractBackEndDataProvider<TechCard, IdTextFilter>() {
         @Override
         protected Stream<TechCard> fetchFromBackEnd(Query<TechCard, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return load(updatedFilter).stream();
         }

         @Override
         protected int sizeInBackEnd(Query<TechCard, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            String sql = "SELECT COUNT(*) " + FROM_WHERE_BLOCK;
            return jdbc.queryForObject(sql, composeArgs(updatedFilter), Integer.class);
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
