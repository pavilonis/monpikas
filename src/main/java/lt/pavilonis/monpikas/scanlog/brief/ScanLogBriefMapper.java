package lt.pavilonis.monpikas.scanlog.brief;

import lt.pavilonis.monpikas.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ScanLogBriefMapper extends SimpleRowMapper<ScanLogBrief> {

   @Override
   public ScanLogBrief mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ScanLogBrief(
            rs.getTimestamp("dateTime").toLocalDateTime(),
            rs.getString("scanner"),
            rs.getString("cardCode"),
            rs.getString("name"),
            rs.getString("group"),
            rs.getString("role"),
            rs.getString("supervisor")
      );
   }
}
