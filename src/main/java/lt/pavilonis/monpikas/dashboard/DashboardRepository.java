package lt.pavilonis.monpikas.dashboard;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardRepository {

   private final NamedParameterJdbcTemplate jdbc;

   public DashboardRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<ScannerEvents> scannerEvents() {
      var sql = "SELECT " +
            "  (SELECT s.name FROM Scanner s WHERE s.id = slOuter.scanner_id), " +
            "  (" +
            "     SELECT count(*) " +
            "     FROM ScanLog slInner " +
            "     WHERE slInner.scanner_id = slOuter.scanner_id AND DATE(slInner.dateTime) = CURDATE()" +
            "  ) AS todayNum," +
            "  COUNT(*) AS totalNum " +
            "FROM ScanLog slOuter " +
            "GROUP BY slOuter.scanner_id " +
            "ORDER BY totalNum DESC";
      return jdbc.query(sql, Map.of(), (rs, i) -> new ScannerEvents(rs.getString(1), rs.getInt(2), rs.getInt(3)));
   }
}
