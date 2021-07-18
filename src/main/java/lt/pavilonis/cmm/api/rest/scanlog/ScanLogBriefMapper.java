package lt.pavilonis.cmm.api.rest.scanlog;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

final class ScanLogBriefMapper extends SimpleRowMapper<ScanLogBrief> {

   @Override
   public ScanLogBrief mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ScanLogBrief(
            rs.getTimestamp("dateTime").toLocalDateTime(),
            rs.getString("scannerName"),
            rs.getString("cardCode"),
            rs.getString("name"),
            rs.getString("organizationGroup"),
            rs.getString("organizationRole"),
            rs.getString("supervisor")
      );
   }
}
