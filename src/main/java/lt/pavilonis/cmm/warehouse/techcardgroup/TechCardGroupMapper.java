package lt.pavilonis.cmm.warehouse.techcardgroup;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TechCardGroupMapper extends SimpleRowMapper<TechCardGroup> {

   @Override
   public TechCardGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new TechCardGroup(
            rs.getLong("tcg.id"),
            rs.getString("tcg.name")
      );
   }
}
