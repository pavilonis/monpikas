package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TechCardResultSetExtractor implements ResultSetExtractor<List<TechCard>> {

   private static final TechCardRowMapper TECH_CARD_MAPPER = new TechCardRowMapper();
   private static final ProductGroupMapper PRODUCT_GROUP_MAPPER = new ProductGroupMapper();

   @Override
   public List<TechCard> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, TechCard> result = new HashMap<>();

      while (rs.next()) {
         mapRow(rs, result);
      }

      return new ArrayList<>(result.values());
   }

   protected void mapRow(ResultSet rs, Map<Long, TechCard> result) throws SQLException {
      TechCard techCard = result
            .computeIfAbsent(rs.getLong("tc.id"), id -> TECH_CARD_MAPPER.mapRow(rs));

      addOutputWeight(rs, techCard.getProductGroupOutputWeight());
   }

   private void addOutputWeight(ResultSet rs, Map<ProductGroup, Integer> weightMap) throws SQLException {
      Integer outputWeight = (Integer) rs.getObject("tcp.outputWeight");
      if (outputWeight != null) {
         ProductGroup productGroup = PRODUCT_GROUP_MAPPER.mapRow(rs);
         weightMap.putIfAbsent(productGroup, outputWeight);
      }
   }
}
