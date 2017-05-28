package lt.pavilonis.cmm.api.tcp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TcpEventStringProcessor {

   private final Logger LOG;
   private static final String FIELD_OPERATION_DESCRIPTION = "OperationDescription";
   private static final String FIELD_DOOR_NUMBER = "DoorName";
   private static final String FIELD_CARD_CODE = "UserCardSerialNumber";
   private static final String STRING_EMPTY = "";
   private static final byte LINES_SEARCH_MAX = 7;
   private final TcpEventDao dao;

   private String operation;
   private byte counter;

   @Autowired
   public TcpEventStringProcessor(TcpEventDao dao) {
      this.dao = dao;
      this.LOG = LoggerFactory.getLogger(TcpEventStringProcessor.class.getSimpleName());
   }

   /**
    * For testing
    */
   public TcpEventStringProcessor(TcpEventDao mockDao, Logger mockLogger) {
      this.LOG = mockLogger;
      this.dao = mockDao;
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
         LOG.info("Classroom event [classroomNumber={}, operation={}]", number, operation);
         int classNumber = Integer.parseInt(number);
         dao.storeClassroomOccupancyEvent(operation, classNumber);

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
         dao.storeScanLogEvent(cardCode);
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
