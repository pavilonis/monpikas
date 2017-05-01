package lt.pavilonis.cmm.warehouse.dishItem;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdNameFilter;
import lt.pavilonis.cmm.warehouse.dish.Dish;
import lt.pavilonis.cmm.warehouse.dish.DishMapper;
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
public class DishItemRepository implements EntityRepository<DishItem, Long, IdNameFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(DishItemRepository.class);
   private static final RowMapper<Dish> MAPPER_DISH = new DishMapper();
   private static final RowMapper<ProductGroup> MAPPER_PRODUCT_GROUP = new ProductGroupMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public DishItem saveOrUpdate(DishItem entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("productGroupId", entity.getProductGroup().getId());
      args.put("dishId", entity.getDish().getId());
      args.put("outputWeight", entity.getOutputWeight());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private DishItem update(Map<String, ?> args) {
      jdbc.update(
            "UPDATE Dish SET name = :name, dishGroup_id = :dishGroupId WHERE id = :id",
            args
      );
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private DishItem create(Map<String, Object> args) {
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
   public List<DishItem> load(IdNameFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, filter.getId());

      List<DishItem> result = jdbc.query("" +
                  "SELECT " +
                  "  di.id, di.outputWeight, " +
                  "  pg.id, pg.name, pg.kcal100, " +
                  "  d.id, d.name," +
                  "  dg.id, dg.name " +
                  "FROM DishItem di " +
                  "  JOIN ProductGroup pg ON pg.id = di.productGroup_id " +
                  "  JOIN Dish d ON d.id = di.dish_id " +
                  "  JOIN DishGroup dg ON dg.id = d.dishGroup_id " +
                  "WHERE :id IS NULL OR d.id = :id " +
                  "ORDER BY d.name",
            args,
            (rs, i) -> new DishItem(
                  rs.getLong("di.id"),
                  MAPPER_DISH.mapRow(rs, i),
                  MAPPER_PRODUCT_GROUP.mapRow(rs, i),
                  rs.getBigDecimal("di.outputWeight")
            )
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]",
            result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<DishItem> find(Long id) {
      List<DishItem> result = load(new IdNameFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM DishItem WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<DishItem> entityClass() {
      return DishItem.class;
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
