package lt.pavilonis.cmm.api.rest.scanlog;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.key.Key;
import lt.pavilonis.cmm.api.rest.key.KeyRepository;
import lt.pavilonis.cmm.api.rest.user.User;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ScanLogRepository {

   private final Logger LOG = LoggerFactory.getLogger(ScanLogRepository.class.getSimpleName());

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Autowired
   private KeyRepository keyRepository;

   @Autowired
   private UserRepository userRepository;

   public ScanLog saveCheckedAndLoad(long scannerId, String cardCode) {

      Long scanLogId = saveChecked(scannerId, cardCode);

      return scanLogId == null
            ? null
            : loadById(scannerId, scanLogId);
   }

   private ScanLog loadById(long scannerId, long scanLogId) {
      return jdbcSalto.queryForObject(
            "SELECT cardCode, dateTime FROM mm_ScanLog WHERE id = :id",
            Collections.singletonMap("id", scanLogId),
            (rs, i) -> {
               String loadedCardCode = rs.getString(1);
               User user = userRepository.load(loadedCardCode, true);
               List<Key> keys = keyRepository.loadActive(scannerId, loadedCardCode, null);
               return new ScanLog(rs.getTimestamp(2).toLocalDateTime(), user, keys);
            }
      );
   }

   public Long saveChecked(long scannerId, String cardCode) {

      if (!userRepository.exists(cardCode)) {
         LOG.warn("Skipping scan log: user not found [scannerId={}, cardCode={}]",
               scannerId, cardCode);
         return null;
      }

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
