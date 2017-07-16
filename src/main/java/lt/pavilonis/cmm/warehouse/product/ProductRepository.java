package lt.pavilonis.cmm.warehouse.product;

import com.vaadin.data.provider.Query;
import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.SizeConsumingBackendDataProvider;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import lt.pavilonis.util.QueryUtils;
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
public class ProductRepository implements EntityRepository<Product, Long, IdTextFilter> {

   private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class.getSimpleName());

   private static final String FROM_WHERE_BLOCK = "" +
         "FROM Product p " +
         "  JOIN ProductGroup pg ON pg.id = p.productGroup_id " +
         "WHERE (:id IS NULL OR p.id = :id) AND (:name IS NULL OR p.name LIKE :name) ";
   private static final RowMapper<Product> MAPPER = new ProductMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public Product saveOrUpdate(Product entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("name", entity.getName());
      args.put("measureUnit", entity.getMeasureUnit().name());
      args.put("unitWeight", entity.getUnitWeight());
      args.put("productGroupId", entity.getProductGroup().getId());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private Product update(Map<String, ?> args) {
      jdbcNamed.update("" +
                  "UPDATE Product " +
                  "SET" +
                  "  name = :name, " +
                  "  measureUnit = :measureUnit, " +
                  "  unitWeight = :unitWeight, " +
                  "  productGroup_id = :productGroupId " +
                  "WHERE id = :id",
            args
      );
      return find((Long) args.get(ID))
            .orElseThrow(IllegalStateException::new);
   }

   private Product create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcNamed.update("" +
                  "INSERT INTO Product (name, measureUnit, unitWeight, productGroup_id) " +
                  "VALUES (:name, :measureUnit, :unitWeight, :productGroupId)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }

   @Override
   public List<Product> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<Product> load(IdTextFilter filter) {
      List<Product> result = jdbcNamed.query("" +
                  "SELECT p.id, p.name, p.measureUnit, p.unitWeight, p.productGroup_id," +
                  "       pg.id, pg.name, pg.kcal100 " +
                  FROM_WHERE_BLOCK +
                  "ORDER BY p.name",
            composeArgs(filter),
            MAPPER
      );
      LOG.info("Loaded [number={}, offset={}, limit={}]", result.size(), filter.getOffSet(), filter.getLimit());
      return result;
   }

   @Override
   public Optional<Product> find(Long id) {
      List<Product> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbcNamed.update("DELETE FROM Product WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<Product> entityClass() {
      return Product.class;
   }

   @Override
   public Optional<SizeConsumingBackendDataProvider<Product, IdTextFilter>> lazyDataProvider(IdTextFilter filter) {
      SizeConsumingBackendDataProvider<Product, IdTextFilter> provider = new SizeConsumingBackendDataProvider<Product, IdTextFilter>() {
         @Override
         protected Stream<Product> fetchFromBackEnd(Query<Product, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return load(updatedFilter).stream();
         }

         @Override
         protected int sizeInBackEnd() {
            return jdbcNamed.queryForObject(
                  "SELECT COUNT(p.id) " + FROM_WHERE_BLOCK,
                  composeArgs(filter),
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

   //   @Override
//   public Map<Long, BigDecimal> currentBalance() {
//      Map<Long, BigDecimal> balanceMap = dsl().select(
//            PRODUCT.ID,
//            coerce(
//                  RECEIPTITEM.QUANTITY.mul(
//                        DSL.choose(PRODUCT.MEASUREUNIT)
//                              .when(MeasureUnit.GRAM.name(), PRODUCT.UNITWEIGHT)
//                              .when(MeasureUnit.PIECE.name(), 1)
//                  ).minus(sum(coalesce(WRITEOFFITEM.QUANTITY, BigDecimal.ZERO))),
//                  BigDecimal.class
//            )
//      )
//            .from(PRODUCT)
//            .join(RECEIPTITEM).on(RECEIPTITEM.PRODUCTID.eq(PRODUCT.ID))
//            .leftJoin(WRITEOFFITEM).on(WRITEOFFITEM.RECEIPTITEMID.eq(RECEIPTITEM.ID))
////            .groupBy(RECEIPTITEM.ID)
////            .fetch(record -> result.put(record.value1(), record.value2()));
//            .fetchMap(Long.class, BigDecimal.class);
//      return balanceMap;
//   }
}
