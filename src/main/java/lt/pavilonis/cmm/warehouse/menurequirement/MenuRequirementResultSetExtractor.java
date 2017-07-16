package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroup;
import lt.pavilonis.cmm.warehouse.productgroup.ProductGroupMapper;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcard.TechCardRowMapper;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSetResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MenuRequirementResultSetExtractor implements ResultSetExtractor<List<MenuRequirement>> {

   private static final TechCardSetResultSetExtractor TECH_CARD_SET_EXTRACTOR = new TechCardSetResultSetExtractor();
   private static final TechCardRowMapper TECH_CARD_MAPPER = new TechCardRowMapper();
   private static final ProductGroupMapper PRODUCT_GROUP_MAPPER = new ProductGroupMapper();

   @Override
   public List<MenuRequirement> extractData(ResultSet rs) throws SQLException, DataAccessException {

      Map<Long, MenuRequirement> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("mr.id");
         MenuRequirement menu = result.get(id);
         if (menu == null) {
            menu = new MenuRequirement(id, rs.getDate("mr.date").toLocalDate(), new ArrayList<>());
            result.put(id, menu);
         }

         List<TechCardSet> techCardSets = menu.getTechCardSets();
//         TechCardSet techCardSet = findById(techCardSets, rs.getLong("m.id")).orElseGet(() -> {
//            TechCardSet newTechCardSet = TECH_CARD_SET_EXTRACTOR.mapRow(rs);
//            techCardSets.add(newTechCardSet);
//            return newTechCardSet;
//         });
//
//         Set<TechCard> cards = techCardSet.getTechCards();
//
//         TechCard techCard = findById(cards, rs.getLong("tc.id"))
//               .orElseGet(() -> mapTechCard(rs, cards));

//         addOutputWeight(rs, techCard.getProductGroupOutputWeight());
      }
      return new ArrayList<>(result.values());
   }

   protected TechCard mapTechCard(ResultSet rs, Collection<TechCard> cards) {
      TechCard techCard = TECH_CARD_MAPPER.mapRow(rs);
      cards.add(techCard);
      return techCard;
   }

   private void addOutputWeight(ResultSet rs, Map<ProductGroup, Integer> weightMap) throws SQLException {
      Integer outputWeight = (Integer) rs.getObject("tcp.outputWeight");
      if (outputWeight != null) {
         ProductGroup productGroup = PRODUCT_GROUP_MAPPER.mapRow(rs);
         weightMap.putIfAbsent(productGroup, outputWeight);
      }
   }

   protected <T extends Identified<Long>> Optional<T> findById(Collection<T> entities, long checkId) {
      return entities.stream()
            .filter(e -> e.getId().equals(checkId))
            .findFirst();
   }
}
