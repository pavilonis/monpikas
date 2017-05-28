package lt.pavilonis.cmm.api.rest.scanlog;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.key.KeyRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import lt.pavilonis.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScanLogRepository {

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Autowired
   private KeyRepository keyRepository;

   @Autowired
   private UserRepository userRepository;

   public ScanLog save(long scannerId, String cardCode) {
      long scanLogId = writeScanLog(scannerId, cardCode);
      return loadById(scannerId, scanLogId);
   }

   private ScanLog loadById(long scannerId, long scanLogId) {
      return jdbcSalto.queryForObject(
            "SELECT cardCode, dateTime FROM mm_ScanLog WHERE id = :id",
            Collections.singletonMap("id", scanLogId),
            (rs, i) -> {
               String loadedCardCode = rs.getString(1);
               User user = userRepository.load(loadedCardCode, true);
               List<Key> keys = keyRepository.loadAssigned(scannerId, loadedCardCode, null);
               return new ScanLog(rs.getTimestamp(2).toLocalDateTime(), user, keys);
            }
      );
   }

   public long writeScanLog(long scannerId, String cardCode) {
      KeyHolder holder = new GeneratedKeyHolder();

      jdbcSalto.update(
            "INSERT INTO mm_ScanLog (cardCode, scanner_id) VALUES (:cardCode, :scannerId)",
            new MapSqlParameterSource(ImmutableMap.<String, Object>of(
                  "cardCode", cardCode,
                  "scannerId", scannerId
            )),
            holder
      );
      return holder.getKey().longValue();
   }
}
