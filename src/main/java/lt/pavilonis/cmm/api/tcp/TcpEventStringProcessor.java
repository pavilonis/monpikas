package lt.pavilonis.cmm.api.tcp;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TcpEventStringProcessor implements MessageHandler {

   private final Logger LOG;
   private static final Map<String, Boolean> CLASSROOM_OPS = ImmutableMap.of(
         "Privacystarted", true,
         "Endofprivacy", false
   );
   private static final int SCANNER_ID_DOORS = 5;
   private static final String FIELD_OPERATION_DESCRIPTION = "OperationDescription";
   private static final String FIELD_DOOR_NAME = "DoorName";
   private static final String FIELD_CARD_CODE = "UserCardSerialNumber";
   private static final String STRING_EMPTY = "";
   private final ScanLogRepository scanLogRepository;
   private final ClassroomRepository classroomRepository;

   private String operation;
   private String location;
   private String cardCode;

   @Autowired
   public TcpEventStringProcessor(ScanLogRepository scanLogRepository,
                                  ClassroomRepository classroomRepository) {
      this(
            scanLogRepository,
            classroomRepository,
            LoggerFactory.getLogger(TcpEventStringProcessor.class.getSimpleName())
      );
   }

   /**
    * For testing
    */
   public TcpEventStringProcessor(ScanLogRepository scanLogRepository,
                                  ClassroomRepository classroomRepository,
                                  Logger mockLogger) {

      this.scanLogRepository = scanLogRepository;
      this.classroomRepository = classroomRepository;
      this.LOG = mockLogger;
   }

   @Override
   public void handleMessage(Message<?> message) throws MessagingException {
      String string = convertMessage(message);

      if (StringUtils.isBlank(string)) {
         LOG.warn("empty message");
         return;
      }

      if (string.contains("][") || string.contains("[")) {
         return;
      }

      if (string.contains("{")) {
         LOG.info("=== MESSAGE START ===");
         clearFields();
         return;
      }

      if (string.contains("}")) {
         LOG.info("=== MESSAGE END ===");
         processCollectedData();
         clearFields();
         return;
      }

      if (string.contains(FIELD_CARD_CODE)) {
         cardCode = clean(string, FIELD_CARD_CODE);

      } else if (string.contains(FIELD_OPERATION_DESCRIPTION)) {
         operation = clean(string, FIELD_OPERATION_DESCRIPTION);

      } else if (string.contains(FIELD_DOOR_NAME)) {
         location = clean(string, FIELD_DOOR_NAME);
      }

      LOG.info(">>> " + string);
   }

   protected String convertMessage(Message<?> message) {
      return message == null || message.getPayload() == null
            ? null
            : new String((byte[]) message.getPayload());
   }

   private void processCollectedData() {
      if (StringUtils.isNotBlank(cardCode)) {
         scanLogRepository.saveChecked(SCANNER_ID_DOORS, cardCode, location);
      }

      if (StringUtils.isNotBlank(operation)) {
         LOG.info("Parsing class-occupancy operation");
         Boolean operationBooleanValue = CLASSROOM_OPS.get(operation);
         if (operationBooleanValue == null) {
            LOG.warn("Unknown operation: " + operation);
            return;
         }

         if (!NumberUtils.isDigits(location) || !NumberUtils.isParsable(location)) {
            LOG.warn("Could not parse class number: " + location);
            return;
         }

         LOG.info("Classroom event [classroomNumber={}, operation={}]", location, operation);
         classroomRepository.save(Integer.parseInt(location), operationBooleanValue);
      }
   }

   private void clearFields() {
      operation = null;
      location = null;
      cardCode = null;
   }

   private String clean(String string, String fieldName) {
      List<String> badChars = Arrays.asList(" ", "\"", ",", "\\n", "\n", "\\r", "\r", ":", fieldName);

      for (String toReplace : badChars) {
         string = string.replace(toReplace, STRING_EMPTY);
      }
      return string;
   }
}
