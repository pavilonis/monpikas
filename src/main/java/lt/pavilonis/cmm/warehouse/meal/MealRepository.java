package lt.pavilonis.cmm.warehouse.meal;

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
public class MealRepository implements EntityRepository<Meal, Long, IdTextFilter> {
   private static final Logger LOG = LoggerFactory.getLogger(MealRepository.class);
   private static final String FROM_WHERE_BLOCK = "" +
         "FROM Meal p " +
         "  JOIN MealGroup pg ON pg.id = p.productGroup_id " +
         "WHERE (:id IS NULL OR p.id = :id) AND (:name IS NULL OR p.name LIKE :name) ";
   private static final RowMapper<Meal> MAPPER = new MealMapper();

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   @Override
   public Meal saveOrUpdate(Meal entity) {
      Map<String, Object> args = new HashMap<>();
      args.put(ID, entity.getId());
      args.put("type", entity.getType());

      return entity.getId() == null
            ? create(args)
            : update(args);
   }

   private Meal update(Map<String, ?> args) {
      jdbc.update("" +
                  "UPDATE Meal " +
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

   private Meal create(Map<String, Object> args) {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbc.update("" +
                  "INSERT INTO Meal (name, measureUnit, unitWeight, productGroup_id) " +
                  "VALUES (:name, :measureUnit, :unitWeight, :productGroupId)",
            new MapSqlParameterSource(args),
            keyHolder
      );
      return find(keyHolder.getKey().longValue())
            .orElseThrow(IllegalStateException::new);
   }


   @Override
   public List<Meal> load(IdTextFilter filter) {
      List<Meal> result = jdbc.query("" +
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
   public Optional<Meal> find(Long id) {
      List<Meal> result = load(new IdTextFilter(id));
      return result.isEmpty()
            ? Optional.empty()
            : Optional.of(result.get(0));
   }

   @Override
   public void delete(Long id) {
      jdbc.update("DELETE FROM Meal WHERE id = :id", Collections.singletonMap("id", id));
   }

   @Override
   public Class<Meal> entityClass() {
      return Meal.class;
   }

   @Override
   public Optional<BackEndDataProvider<Meal, IdTextFilter>> lazyDataProvider(IdTextFilter filter) {
      BackEndDataProvider<Meal, IdTextFilter> provider = new AbstractBackEndDataProvider<Meal, IdTextFilter>() {
         @Override
         protected Stream<Meal> fetchFromBackEnd(Query<Meal, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return load(updatedFilter).stream();
         }

         @Override
         protected int sizeInBackEnd(Query<Meal, IdTextFilter> query) {
            IdTextFilter updatedFilter = filter
                  .withOffset(query.getOffset())
                  .withLimit(query.getLimit());

            return jdbc.queryForObject(
                  "SELECT COUNT(p.id) " + FROM_WHERE_BLOCK,
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
