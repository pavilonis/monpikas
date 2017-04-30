//package lt.pavilonis.cmm.warehouse.supplier;
//
//import lt.pavilonis.wh.common.JooqRepository;
//import lt.pavilonis.wh.common.util.IdExtractor;
//import lt.pavilonis.wh.domain.generated.tables.records.SupplierRecord;
//import lt.pavilonis.wh.product.resource.SupplierResource;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static lt.pavilonis.wh.domain.generated.Tables.SUPPLIER;
//
//@Repository
//public class SupplierRepository extends JooqRepository implements SupplierRepository {
//
//   private static final String SELF = "self";
//
//   @Override
//   public List<SupplierRecord> loadAll() {
//      return dsl().selectFrom(SUPPLIER)
//            .fetch();
//   }
//
//   @Override
//   public SupplierRecord load(long id) {
//      return dsl().selectFrom(SUPPLIER)
//            .where(SUPPLIER.ID.eq(id))
//            .fetchOne();
//   }
//
//
//   @Override
//   public SupplierRecord update(Long id, SupplierResource resource) {
//      dsl().update(SUPPLIER)
//            .set(SUPPLIER.CODE, resource.getCode())
//            .set(SUPPLIER.NAME, resource.getName())
//            .where(SUPPLIER.ID.equal(IdExtractor.idFrom(resource.getLink(SELF))))
//            .execute();
//
//      return dsl().selectFrom(SUPPLIER)
//            .where(SUPPLIER.ID.eq(id))
//            .fetchOne();
//   }
//
//   @Override
//   public SupplierRecord create(SupplierResource resource) {
//      return dsl().insertInto(SUPPLIER)
//            .columns(SUPPLIER.CODE, SUPPLIER.NAME)
//            .values(resource.getCode(), resource.getName())
//            .returning()
//            .fetchOne();
//   }
//
//   @Override
//   public int delete(long id) {
//      return dsl().deleteFrom(SUPPLIER)
//            .where(SUPPLIER.ID.eq(id))
//            .execute();
//   }
//}
