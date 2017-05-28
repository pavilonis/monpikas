package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechCardGroupMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TechCardRowMapper extends SimpleRowMapper<TechCard> {

   private static final TechCardGroupMapper TECH_CARD_GROUP_MAPPER = new TechCardGroupMapper();

   @Override
   public TechCard mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new TechCard(
            rs.getLong("tc.id"),
            rs.getString("tc.name"),
            TECH_CARD_GROUP_MAPPER.mapRow(rs),
            new HashMap<>()
      );
   }
}
