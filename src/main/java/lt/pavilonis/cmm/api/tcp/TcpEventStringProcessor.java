package lt.pavilonis.cmm.api.tcp;

import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TcpEventStringProcessor implements MessageHandler {

   private final Logger logger;
   private static final int SCANNER_ID_DOORS = 5;
   private static final String FIELD_OPERATION_DESCRIPTION = "OperationDescription";
   private static final String FIELD_DOOR_NAME = "DoorName";
   private static final String FIELD_CARD_CODE = "UserCardSerialNumber";
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
                                  ClassroomRepository classroomRepository, Logger mockLogger) {

      this.scanLogRepository = scanLogRepository;
      this.classroomRepository = classroomRepository;
      this.logger = mockLogger;
   }

   @Override
   public void handleMessage(Message<?> message) {
      String string = convertMessage(message);

      if (StringUtils.isBlank(string)) {
         logger.warn("empty message");
         return;
      }

      if (string.contains("][") || string.contains("[")) {
         return;
      }

      if (string.contains("{")) {
         logger.info("=== MESSAGE START ===");
         clearFields();
         return;
      }

      if (string.contains("}")) {
         logger.info("=== MESSAGE END ===");
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

      logger.info(">>> {}", string);
   }

   private void processCollectedData() {
      if (StringUtils.isNotBlank(cardCode)) {
         scanLogRepository.saveChecked(SCANNER_ID_DOORS, cardCode, location);
      }

      if (StringUtils.isBlank(operation)) {
         return;
      }

      logger.info("Parsing class-occupancy operation");
      Optional<ClassroomAction> action = ClassroomAction.forCode(operation);
      if (!action.isPresent()) {
         logger.warn("Unknown operation: {}", operation);
         return;
      }

      Optional<Classroom> classroom = findClassroom(location);
      if (classroom.isPresent()) {
         logger.info("Classroom event [classroomNumber={}, operation={}]", location, operation);
         classroomRepository.save(classroom.get(), action.get().getBooleanValue());
      } else {
         logger.warn("Could not find building/class number from: {}", location);
      }
   }

   private Optional<Classroom> findClassroom(String location) {
      if (StringUtils.isBlank(location)) {
         logger.warn("Location is empty!");
         return Optional.empty();
      }

      if (isNumber(location)) {
         return Optional.of(new Classroom(Building.SCHOOL, Integer.parseInt(location)));
      }

      char buildingCode = location.charAt(0);
      Optional<Building> optBuilding = Building.forCode(buildingCode);

      return optBuilding.flatMap(building -> {
         String remainingString = location.substring(1);
         return isNumber(remainingString)
               ? Optional.of(new Classroom(building, Integer.parseInt(remainingString)))
               : Optional.empty();
      });
   }

   private void clearFields() {
      operation = null;
      location = null;
      cardCode = null;
   }

   private String clean(String string, String fieldName) {
      List<String> badChars = Arrays.asList(" ", "\"", ",", "\\n", "\n", "\\r", "\r", ":", fieldName);

      for (String toReplace : badChars) {
         string = string.replace(toReplace, StringUtils.EMPTY);
      }
      return string;
   }

   private boolean isNumber(String string) {
      return NumberUtils.isDigits(string) && NumberUtils.isParsable(string);
   }

   protected String convertMessage(Message<?> message) {
      return message == null || message.getPayload() == null
            ? null
            : new String((byte[]) message.getPayload());
   }
}
