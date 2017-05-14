package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;
import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TechnologicalCardMapper extends SimpleRowMapper<TechnologicalCard> {

   @Override
   public TechnologicalCard mapRow(ResultSet rs, int i) throws SQLException {

      return new TechnologicalCard(
            rs.getLong("tc.id"),
            rs.getString("tc.name"),
            new TechnologicalCardGroup(
                  rs.getLong("tcg.id"),
                  rs.getString("tcg.name")
            )
      );
   }
}
