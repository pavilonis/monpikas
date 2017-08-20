package lt.pavilonis.cmm.warehouse.menurequirement;

import lt.pavilonis.cmm.common.Identified;
import lt.pavilonis.cmm.warehouse.techcard.TechCard;
import lt.pavilonis.cmm.warehouse.techcard.TechCardResultSetExtractor;
import lt.pavilonis.cmm.warehouse.techcard.TechCardRowMapper;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSet;
import lt.pavilonis.cmm.warehouse.techcardset.TechCardSetResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRequirementResultSetExtractor implements ResultSetExtractor<List<MenuRequirement>> {

   private static final TechCardRowMapper TECH_CARD_MAPPER = new TechCardRowMapper();

   @Override
   public List<MenuRequirement> extractData(ResultSet rs) throws SQLException, DataAccessException {

      Map<Long, MenuRequirement> result = new HashMap<>();

      while (rs.next()) {
         long id = rs.getLong("mr.id");
         MenuRequirement menu = result.get(id);
         if (menu == null) {
            LocalDate date = rs.getDate("mr.date").toLocalDate();
            result.put(id, menu = new MenuRequirement(id, date, new ArrayList<>()));
         }

         Collection<TechCardSet> techCardSets = menu.getTechCardSets();
         TechCardSet techCardSet = findById(techCardSets, rs.getLong("tcs.id"));
         if (techCardSet == null) {
            techCardSets.add(techCardSet = TechCardSetResultSetExtractor.mapRow(rs));
         }

         Collection<TechCard> cards = techCardSet.getTechCards();

         TechCard techCard = findById(cards, rs.getLong("tc.id"));
         if (techCard == null) {
            cards.add(techCard = TECH_CARD_MAPPER.mapRow(rs));
         }

         TechCardResultSetExtractor.addOutputWeight(rs, techCard.getProductGroupOutputWeight());
      }
      return new ArrayList<>(result.values());
   }

   protected <T extends Identified<Long>> T findById(Collection<T> entities, long checkId) {
      return entities.stream()
            .filter(e -> e.getId().equals(checkId))
            .findFirst()
            .orElse(null);
   }
}
