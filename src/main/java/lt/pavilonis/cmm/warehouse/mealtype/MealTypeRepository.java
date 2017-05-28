package lt.pavilonis.cmm.warehouse.mealtype;

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
public class MealTypeRepository implements EntityRepository<MealType, Long, IdTextFilter> {

   private static final RowMapper<MealType> MAPPER = new MealTypeMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public MealType saveOrUpdate(MealType entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private MealType update(Map<String, ?> args) {
      jdbcNamed.update("UPDATE MealType SET name = :name WHERE id = :id", args);
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private MealType create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update("INSERT INTO MealType (name) VALUE (:name)", new MapSqlParameterSource(args), keyHolder);

      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<MealType> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("id", filter.getId());
      args.put("name", QueryUtils.likeArg(filter.getText()));
      return jdbcNamed.query("" +
                  "SELECT mt.id, mt.name " +
                  "FROM MealType mt " +
                  "WHERE (:id IS NULL OR mt.id = :id) AND (:name IS NULL OR mt.name LIKE :name) " +
                  "ORDER BY mt.name",
            args,
            MAPPER
      );
   }

   @Override
   public Optional<MealType> find(Long id) {
      List<MealType> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM MealType WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<MealType> entityClass() {
      return MealType.class;
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
