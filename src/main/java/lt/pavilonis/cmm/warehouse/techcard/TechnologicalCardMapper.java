package lt.pavilonis.cmm.warehouse.techcard;

import lt.pavilonis.cmm.warehouse.techcardgroup.TechnologicalCardGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TechnologicalCardMapper implements RowMapper<TechnologicalCard> {

   @Override
   public TechnologicalCard mapRow(ResultSet rs, int i) throws SQLException {
      return new TechnologicalCard(
            rs.getLong("tc.id"),
            rs.getString("tc.name"),
            new TechnologicalCardGroup(rs.getLong("dg.id"), rs.getString("dg.name"))
      );
   }
}
