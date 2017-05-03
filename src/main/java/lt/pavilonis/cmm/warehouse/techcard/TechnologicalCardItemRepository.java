package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
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

@Repository
public class TechnologicalCardItemRepository implements EntityRepository<TechnologicalCardItem, Long, IdNameFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(TechnologicalCardItemRepository.class);
   private static final RowMapper<TechnologicalCard> MAPPER_TECH_CARD = new TechnologicalCardMapper();
   private static final RowMapper<ProductGroup> MAPPER_PRODUCT_GROUP = new ProductGroupMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public TechnologicalCardItem saveOrUpdate(TechnologicalCardItem entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("productGroupId", entity.getProductGroup().getId());
      args.put("dishId", entity.getDish().getId());
      args.put("outputWeight", entity.getOutputWeight());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private TechnologicalCardItem update(Map<String, ?> args) {
      jdbc.update(
            "UPDATE Dish SET name = :name, dishGroup_id = :dishGroupId WHERE id = :id",
            args
      );
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private TechnologicalCardItem create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update("" +
                  "INSERT INTO DishItem (dish_id, productGroup_id, outputWeight) " +
                  "VALUES (:dishId, :productGroupId, :outputWeight)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }


   @Override
   public List<TechnologicalCardItem> load(IdNameFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, filter.getId());

      List<TechnologicalCardItem> result = jdbc.query("" +
                  "SELECT " +
                  "  tci.id, tci.outputWeight, " +
                  "  pg.id, pg.name, pg.kcal100, " +
                  "  tc.id, tc.name," +
                  "  dg.id, dg.name " +
                  "FROM TechnologicalCardItem tci " +
                  "  JOIN ProductGroup pg ON pg.id = di.productGroup_id " +
                  "  JOIN TechnologicalCard tc ON tc.id = di.dish_id " +
                  "  JOIN DishGroup dg ON dg.id = tc.dishGroup_id " +
                  "WHERE :id IS NULL OR tci.id = :id " +
                  "ORDER BY tc.name",
            args,
            (rs, i) -> new TechnologicalCardItem(
                  rs.getLong("di.id"),
                  MAPPER_TECH_CARD.mapRow(rs, i),
                  MAPPER_PRODUCT_GROUP.mapRow(rs, i),
                  rs.getBigDecimal("di.outputWeight")
            )
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]",
            result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<TechnologicalCardItem> find(Long id) {
      List<TechnologicalCardItem> result = load(new IdNameFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM DishItem WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<TechnologicalCardItem> entityClass() {
      return TechnologicalCardItem.class;
   }

   //TODO
//   @Override
//   public BigDecimal kcal(Long dishItemId) {
//      return dsl().select(PRODUCTGROUP.KCAL100.div(100).mul(DISHITEM.OUTPUTWEIGHT).coerce(BigDecimal.class))
//            .from(PRODUCTGROUP)
//            .join(DISHITEM).on(DISHITEM.PRODUCTGROUPID.eq(PRODUCTGROUP.ID)).and(DISHITEM.ID.eq(dishItemId))
//            .fetchOne()
//            .value1();
//   }
}
