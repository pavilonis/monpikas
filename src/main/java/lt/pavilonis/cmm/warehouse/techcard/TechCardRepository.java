package lt.pavilonis.cmm.warehouse.techcard;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.util.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class TechCardRepository implements EntityRepository<TechCard, Long, IdTextFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(TechCardRepository.class.getSimpleName());

   private static final String FROM_WHERE_BLOCK = "" +
         "FROM TechCard tc " +
         "  JOIN TechCardGroup tcg ON tcg.id = tc.techCardGroup_id " +
         "  LEFT JOIN TechCardProduct tcp ON tcp.techCard_id = tc.id " +
         "  LEFT JOIN ProductGroup pg ON pg.id = tcp.productGroup_id " +
         "WHERE (:id IS NULL OR tc.id = :id) AND (:name IS NULL OR tc.name LIKE :name) ";

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Transactional
   @Override
   public TechCard saveOrUpdate(TechCard card) {

      Map<String, Object> args = new HashMap<>();
      args.put(ID, card.getId());
      args.put("name", card.getName());
      args.put("groupId", card.getGroup().getId());
      args.put("dateCreated", new Date());

      return card.getId() == null
            ? create(card, args)
            : update(card, args);
   }

   private TechCard update(TechCard card, Map<String, Object> args) {

      jdbcNamed.update("UPDATE TechCard SET name = :name, techCardGroup_id = :groupId WHERE id = :id", args);
      jdbcNamed.update("DELETE FROM TechCardProduct WHERE techCard_id = :id", args);
      saveTechCardProducts(card.getId(), card.getProductGroupOutputWeight());

      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private void saveTechCardProducts(long cardId, Map<ProductGroup, Integer> outputWeights) {

      String query = "INSERT INTO TechCardProduct (techCard_id, productGroup_id, outputWeight, dateCreated) " +
            "VALUES (:cardId, :productGroupId, :outputWeight, :dateCreated)";

      Date now = new Date();
      outputWeights.forEach((productGroup, outputWeight) -> jdbcNamed.update(query, ImmutableMap.of(
            "cardId", cardId,
            "productGroupId", productGroup.getId(),
            "outputWeight", outputWeight,
            "dateCreated", now
      )));
   }

   private TechCard create(TechCard card, Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();

      jdbcNamed.update("" +
                  "INSERT INTO TechCard (name, techCardGroup_id, dateCreated) " +
                  "VALUES (:name, :groupId, :dateCreated)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      saveTechCardProducts(keyHolder.getKey().longValue(), card.getProductGroupOutputWeight());

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<TechCard> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<TechCard> load(IdTextFilter filter) {
      List<TechCard> result = jdbcNamed.query("" +
                  "SELECT " +
                  "  tc.id, tc.name, " +
                  "  tcg.id, tcg.name," +
                  "  tcp.outputWeight," +
                  "  pg.id, pg.name, pg.kcal100 " +
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
      jdbcNamed.update("DELETE FROM TechCard WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechCard> entityClass() {
      return TechCard.class;
   }

   @Override
   public Optional<SizeConsumingBackendDataProvider<TechCard, IdTextFilter>> lazyDataProvider(IdTextFilter filter) {
      SizeConsumingBackendDataProvider<TechCard, IdTextFilter> provider =
            new SizeConsumingBackendDataProvider<TechCard, IdTextFilter>() {
               @Override
               protected Stream<TechCard> fetchFromBackEnd(Query<TechCard, IdTextFilter> query) {
                  IdTextFilter updatedFilter = filter
                        .withOffset(query.getOffset())
                        .withLimit(query.getLimit());

                  return load(updatedFilter).stream();
               }

               @Override
               protected int sizeInBackEnd() {
                  String sql = "SELECT COUNT(*) " + FROM_WHERE_BLOCK;
                  return jdbcNamed.queryForObject(sql, composeArgs(filter), Integer.class);
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
}
