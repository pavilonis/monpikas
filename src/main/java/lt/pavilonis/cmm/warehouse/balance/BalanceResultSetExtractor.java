package lt.pavilonis.cmm.warehouse.balance;

import lt.pavilonis.cmm.warehouse.product.Product;
import lt.pavilonis.cmm.warehouse.product.ProductMapper;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceResultSetExtractor implements ResultSetExtractor<List<Balance>> {

   private final ProductGroupMapper productGroupMapper = new ProductGroupMapper();
   private final ProductMapper productMapper = new ProductMapper();

   @Override
   public List<Balance> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, Balance> productGroupBalanceMap = new HashMap<>();

      while (rs.next()) {

         long productGroupId = rs.getLong("pg.id");
         Balance balance = productGroupBalanceMap.get(productGroupId);

         if (balance == null) {
            ProductGroup productGroup = productGroupMapper.mapRow(rs);
            balance = new Balance(productGroup, new HashMap<>());
            productGroupBalanceMap.put(productGroupId, balance);
         }

         Map<Product, BigDecimal> quantityMap = balance.getQuantity();

         Product product = productMapper.mapRow(rs);
         quantityMap.put(product, rs.getBigDecimal("balance"));
      }

      return new ArrayList<>(productGroupBalanceMap.values());
   }
}
