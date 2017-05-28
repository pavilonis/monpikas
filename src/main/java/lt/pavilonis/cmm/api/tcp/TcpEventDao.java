package lt.pavilonis.cmm.api.tcp;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import lt.pavilonis.cmm.api.rest.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TcpEventDao {

   private final Logger LOG = LoggerFactory.getLogger(TcpEventDao.class.getSimpleName());
   private static final int SCANNER_ID_DOORS = 5;
   public static final String OPERATION_PRIVACY_START = "Privacystarted";
   public static final String OPERATION_PRIVACY_END = "Endofprivacy";
   private static final Map<String, Boolean> CLASSROOM_OPS = ImmutableMap.of(
         OPERATION_PRIVACY_START, true,
         OPERATION_PRIVACY_END, false
   );

   @Autowired
   private NamedParameterJdbcTemplate jdbcSalto;

   @Autowired
   private ScanLogRepository scanLogRepository;

   @Autowired
   private UserRepository users;

   public void storeScanLogEvent(String cardCode) {
      if (!users.exists(cardCode)) {
         LOG.warn("Skipping scan log: user not found [cardCode={}]", cardCode);
         return;
      }
      scanLogRepository.writeScanLog(SCANNER_ID_DOORS, cardCode);
   }

   public void storeClassroomOccupancyEvent(String operation, int classroomNumber) {

      Boolean operationBooleanValue = CLASSROOM_OPS.get(operation);
      if (operationBooleanValue == null) {
         LOG.warn("Unknown operation: " + operation);
         return;
      }

      jdbcSalto.update(
            "INSERT INTO mm_ClassroomOccupancy (classroomNumber, occupied) VALUES (:number, :operation)",
            ImmutableMap.of("number", classroomNumber, "operation", operationBooleanValue)
      );
   }
}
