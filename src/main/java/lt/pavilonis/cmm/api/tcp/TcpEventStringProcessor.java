package lt.pavilonis.cmm.api.tcp;

import com.google.common.collect.ImmutableMap;
import lt.pavilonis.cmm.api.rest.classroom.ClassroomRepository;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TcpEventStringProcessor {

   private final Logger LOG;
   private static final Map<String, Boolean> CLASSROOM_OPS = ImmutableMap.of(
         "Privacystarted", true,
         "Endofprivacy", false
   );
   private static final int SCANNER_ID_DOORS = 5;
   private static final String FIELD_OPERATION_DESCRIPTION = "OperationDescription";
   private static final String FIELD_DOOR_NUMBER = "DoorName";
   private static final String FIELD_CARD_CODE = "UserCardSerialNumber";
   private static final String STRING_EMPTY = "";
   private static final byte LINES_SEARCH_MAX = 7;
   private final ScanLogRepository scanLogRepository;
   private final ClassroomRepository classroomRepository;

   private String operation;
   private byte counter;

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

   public void process(String inputNonNull) {

      if (counter < LINES_SEARCH_MAX) {
         counter++;
      }

      if (inputNonNull.contains(FIELD_CARD_CODE)) {

         processScanLog(inputNonNull);

      } else if (inputNonNull.contains(FIELD_OPERATION_DESCRIPTION)) {

         processOperationStart(inputNonNull);

      } else if (counter < LINES_SEARCH_MAX && inputNonNull.contains(FIELD_DOOR_NUMBER)) {

         processOperationFinish(inputNonNull);

      } else {

         LOG.warn("Skip: " + inputNonNull);

      }
   }

   private void processOperationFinish(String input) {
      String number = clean(input, FIELD_DOOR_NUMBER);

      if (NumberUtils.isDigits(number) && NumberUtils.isParsable(number)) {

         Boolean operationBooleanValue = CLASSROOM_OPS.get(operation);
         if (operationBooleanValue == null) {
            LOG.warn("Unknown operation: " + operation);
            return;
         }

         LOG.info("Classroom event [classroomNumber={}, operation={}]", number, operation);
         int classNumber = Integer.parseInt(number);

         classroomRepository.save(classNumber, operationBooleanValue);

      } else {
         LOG.warn("Bad number, skipping: " + input);
      }
   }

   private void processOperationStart(String input) {
      String operationString = clean(input, FIELD_OPERATION_DESCRIPTION);

      if (StringUtils.isNotBlank(operationString)) {
         operation = operationString;
         counter = 0;
      }
   }

   private void processScanLog(String input) {
      String cardCode = clean(input, FIELD_CARD_CODE);

      if (StringUtils.isNotBlank(cardCode)) {

         scanLogRepository.saveChecked(SCANNER_ID_DOORS, cardCode);
      } else {
         LOG.error("Blank cardCode!");
      }
   }

   private String clean(String string, String fieldName) {
      string = string
            .replace(" ", STRING_EMPTY)
            .replace("\"", STRING_EMPTY)
            .replace(",", STRING_EMPTY)
            .replace("\\n", STRING_EMPTY)
            .replace(":", STRING_EMPTY);

      return string.replace(fieldName, STRING_EMPTY);
   }
}
