package lt.pavilonis.cmm.warehouse.balance;

import lt.pavilonis.cmm.common.EntityRepository;
import lt.pavilonis.cmm.common.ui.filter.IdTextFilter;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class BalanceRepository implements EntityRepository<Balance, Void, IdTextFilter> {

   @Autowired
   private NamedParameterJdbcTemplate jdbcNamed;

   @Override
   public Balance saveOrUpdate(Balance entity) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public List<Balance> load() {
      return load(IdTextFilter.empty());
   }

   @Override
   public List<Balance> load(IdTextFilter filter) {
      Map<String, Object> args = new HashMap<>();
//      args.put("name", QueryUtils.likeArg(filter.getText()));

      return jdbcNamed.query("" +
                  "SELECT " +
                  "  p.id, p.name, p.measureUnit, p.unitWeight, " +
                  "  pg.id, pg.name, pg.kcal100," +
                  "  SUM(IF(p.measureUnit = 'GRAM', ri.quantity * p.unitWeight, ri.quantity)) - SUM(COALESCE(woi.quantity, 0)) AS balance " +
                  "FROM Product p " +
                  "  JOIN ProductGroup pg ON pg.id = p.productGroup_id " +
                  "  JOIN ReceiptItem ri ON ri.product_id = p.id " +
                  "  LEFT JOIN WriteOffItem woi ON woi.receiptItem_id = ri.id " +
                  "GROUP BY p.id " +
                  "ORDER BY pg.name",
            args,
            new BalanceResultSetExtractor()
      );
   }

   @Override
   public Optional<Balance> find(Void id) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public void delete(Void id) {
      throw new NotImplementedException("Not needed");
   }

   @Override
   public Class<Balance> entityClass() {
      return Balance.class;
   }
}
