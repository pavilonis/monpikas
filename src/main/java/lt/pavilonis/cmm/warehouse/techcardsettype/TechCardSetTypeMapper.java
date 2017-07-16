package lt.pavilonis.cmm.warehouse.techcardsettype;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TechCardSetTypeMapper extends SimpleRowMapper<TechCardSetType> {

   @Override
   public TechCardSetType mapRow(ResultSet rs, int i) throws SQLException {

      return new TechCardSetType(
            rs.getLong("tcst.id"),
            rs.getString("tcst.name")
      );
   }
}
