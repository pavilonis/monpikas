//package lt.pavilonis.cmm.warehouse.product;
//
//import org.springframework.stereotype.Repository;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//@Repository
//public class ProductRepository extends JooqRepository implements ProductRepository {
//
//   @Override
//   public List<ProductRecord> loadAll() {
//      return dsl().selectFrom(PRODUCT).fetch();
//   }
//
//   @Override
//   public ProductRecord load(long id) {
//      return dsl().selectFrom(PRODUCT).where(PRODUCT.ID.eq(id)).fetchOne();
//   }
//
//   @Override
//   public ProductRecord create(ProductResource res) {
//      return dsl().insertInto(PRODUCT)
//            .columns(PRODUCT.PRODUCTGROUPID, PRODUCT.NAME, PRODUCT.MEASUREUNIT, PRODUCT.UNITWEIGHT)
//            .values(idFrom(res.getLink("productGroup")), res.getName(), res.getMeasureUnit().toString(), res.getUnitWeight())
//            .returning()
//            .fetchOne();
//   }
//
//   @Override
//   public ProductRecord update(Long id, ProductResource resource) {
//      dsl().update(PRODUCT)
//            .set(PRODUCT.PRODUCTGROUPID, idFrom(resource.getLink("productGroup")))
//            .set(PRODUCT.NAME, resource.getName())
//            .set(PRODUCT.MEASUREUNIT, resource.getMeasureUnit().toString())
//            .set(PRODUCT.UNITWEIGHT, resource.getUnitWeight())
//            .where(PRODUCT.ID.eq(id))
//            .execute();
//
//      return dsl().selectFrom(PRODUCT).where(PRODUCT.ID.eq(id)).fetchOne();
//   }
//
//   @Override
//   public int delete(long id) {
//      return dsl().deleteFrom(PRODUCT).where(PRODUCT.ID.eq(id)).execute();
//   }
//
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
//}
