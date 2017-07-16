package lt.pavilonis.cmm.warehouse.techcardset;

import lt.pavilonis.cmm.warehouse.techcardsettype.TechCardSetTypeMapper;
import lt.pavilonis.cmm.warehouse.techcard.TechCardRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class TechCardSetResultSetExtractor implements ResultSetExtractor<List<TechCardSet>> {

   private static final TechCardSetTypeMapper TECH_CARD_SET_TYPE_MAPPER = new TechCardSetTypeMapper();
   private static final TechCardRowMapper TECH_CARD_MAPPER = new TechCardRowMapper();

   @Override
   public List<TechCardSet> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, TechCardSet> result = new HashMap<>();
      while (rs.next()) {
         long id = rs.getLong("tcs.id");
         TechCardSet cardSet = result.get(id);
         if (cardSet == null) {
            result.put(id, cardSet = mapRow(rs));
         }

         Long techCardId = (Long) rs.getObject("tc.id");
         if (techCardId != null
               && cardSet.getTechCards().stream().noneMatch(c -> techCardId.equals(c.getId()))) {
            cardSet.getTechCards().add(TECH_CARD_MAPPER.mapRow(rs));
         }
      }
      return new ArrayList<>(result.values());
   }

   private TechCardSet mapRow(ResultSet rs) throws SQLException {
      return new TechCardSet(
            rs.getLong("tcs.id"),
            rs.getString("tcs.name"),
            TECH_CARD_SET_TYPE_MAPPER.mapRow(rs),
            new HashSet<>()
      );
   }
}
