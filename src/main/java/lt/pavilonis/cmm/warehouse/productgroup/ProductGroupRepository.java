package lt.pavilonis.cmm.warehouse.productgroup;

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
public class ProductGroupRepository implements EntityRepository<ProductGroup, Long, IdNameFilter> {

   private static final RowMapper<ProductGroup> MAPPER =
         (rs, i) -> new ProductGroup(rs.getLong(1), rs.getString(2), rs.getInt(3));

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

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
      jdbc.update("UPDATE ProductGroup SET name = :name, kcal100 = :kcal100 WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private ProductGroup create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update(
            "INSERT INTO ProductGroup (name, kcal100) VALUES (:name, :kcal100)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<ProductGroup> load(IdNameFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("name", QueryUtils.likeArg(filter.getName()));
      return jdbc.query("" +
                  "SELECT id, name, kcal100 " +
                  "FROM ProductGroup " +
                  "WHERE (:id IS NULL OR id = :id) AND (:name IS NULL OR name LIKE :name) " +
                  "ORDER BY name",
            args,
            MAPPER
      );
   }

   @Override
   public Optional<ProductGroup> find(Long id) {
      List<ProductGroup> result = load(new IdNameFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM ProductGroup WHERE id = :id", Collections.singletonMap("id", id));
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
