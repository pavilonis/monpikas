package lt.pavilonis.cmm.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashBoardRepository {

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   public List<ScannerEvents> scannerEvents() {
      var sql = "SELECT " +
            "  (SELECT s.name FROM mm_Scanner s WHERE s.id = slOuter.scanner_id), " +
            "  (" +
            "     SELECT count(*) " +
            "     FROM mm_ScanLog slInner " +
            "     WHERE slInner.scanner_id = slOuter.scanner_id " +
            "        AND CONVERT(date, slInner.dateTime) = CONVERT(date, GETDATE()) " +
            "  ) AS todayNum," +
            "  COUNT(*) AS totalNum " +
            "FROM mm_ScanLog slOuter " +
            "GROUP BY slOuter.scanner_id " +
            "ORDER BY totalNum DESC";
      return jdbc.query(sql, Map.of(), (rs, i) -> new ScannerEvents(rs.getString(1), rs.getInt(2), rs.getInt(3)));
   }
}
