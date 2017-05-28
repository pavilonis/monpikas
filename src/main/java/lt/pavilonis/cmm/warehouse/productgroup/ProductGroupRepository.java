package lt.pavilonis.cmm.warehouse.productgroup;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.util.QueryUtils;
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
public class ProductGroupRepository implements EntityRepository<ProductGroup, Long, IdTextFilter> {

   private static final RowMapper<ProductGroup> MAPPER = new ProductGroupMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public ProductGroup saveOrUpdate(ProductGroup entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());
      args.put("kcal100", entity.getKcal100());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private ProductGroup update(Map<String, ?> args) {
      jdbcNamed.update("UPDATE ProductGroup SET name = :name, kcal100 = :kcal100 WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private ProductGroup create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update(
            "INSERT INTO ProductGroup (name, kcal100) VALUES (:name, :kcal100)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<ProductGroup> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("name", QueryUtils.likeArg(filter.getText()));
      return jdbcNamed.query("" +
                  "SELECT pg.id, pg.name, pg.kcal100 " +
                  "FROM ProductGroup pg " +
                  "WHERE (:id IS NULL OR pg.id = :id) AND (:name IS NULL OR pg.name LIKE :name) " +
                  "ORDER BY pg.name",
            args,
            MAPPER
      );
   }

   @Override
   public Optional<ProductGroup> find(Long id) {
      List<ProductGroup> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM ProductGroup WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<ProductGroup> entityClass() {
      return ProductGroup.class;
   }
//
//   public List<ProductRecord> items(long groupId) {
//      return dsl().selectFrom(PRODUCT)
//            .where(PRODUCT.PRODUCTGROUPID.eq(groupId))
//            .fetch();
//   }
//
//
//   public BigDecimal weightInStock(long groupId) {
//      return dsl()
//            .select(
//                  DSL.sum(
//                        DSL.coalesce(RECEIPTITEM.QUANTITY, BigDecimal.ZERO).multiply(PRODUCT.UNITWEIGHT)
//                              .minus(DSL.coalesce(WRITEOFFITEM.QUANTITY, BigDecimal.ZERO))
//                  )
//            )
//            .from(PRODUCTGROUP)
//            .join(PRODUCT).on(PRODUCT.PRODUCTGROUPID.eq(PRODUCTGROUP.ID))
//            .leftJoin(RECEIPTITEM).on(RECEIPTITEM.PRODUCTID.eq(PRODUCT.ID))
//            .leftJoin(WRITEOFFITEM).on(WRITEOFFITEM.RECEIPTITEMID.eq(RECEIPTITEM.ID))
//            .where(PRODUCTGROUP.ID.eq(groupId))
//            .fetchOneInto(BigDecimal.class);
//   }
}
