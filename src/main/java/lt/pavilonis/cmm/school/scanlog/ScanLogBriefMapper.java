package lt.pavilonis.cmm.school.scanlog;

import lt.pavilonis.cmm.api.rest.scanlog.ScanLogBrief;
import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ScanLogBriefMapper extends SimpleRowMapper<ScanLogBrief> {

   @Override
   public ScanLogBrief mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ScanLogBrief(
            rs.getTimestamp("dateTime").toLocalDateTime(),
            rs.getString("scannerName"),
            rs.getString("cardCode"),
            rs.getString("userName"),
            rs.getString("userGroup"),
            rs.getString("userRole")
      );
   }

}
