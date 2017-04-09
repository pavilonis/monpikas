package lt.pavilonis.cmm.canteen.report;

import lt.pavilonis.cmm.common.service.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.repository.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import lt.pavilonis.cmm.canteen.ui.event.MealEventFilter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportService {

   @Autowired
   private MealEventLogRepository mealEventLogRepository;

   @Autowired
   private MessageSourceAdapter messages;

   private static final Logger LOG = getLogger(UserMealService.class);

   public ByteArrayOutputStream generate(LocalDate periodStart, LocalDate periodEnd, PupilType pupilType) {

      List<MealEventLog> events = mealEventLogRepository
            .load(new MealEventFilter(null, periodStart, periodEnd, pupilType));

      String reportPeriod = DateTimeFormatter.ISO_DATE.format(periodStart) +
            "  -  " + DateTimeFormatter.ISO_DATE.format(periodEnd);

      HSSFWorkbook wb = new Report(messages, reportPeriod, events)
            .create(pupilType);

      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
         wb.write(baos);
         LOG.info("Report generated");
         return baos;
      } catch (IOException e) {
         e.printStackTrace();
         LOG.info("Report Error");
         throw new RuntimeException("Report Error: " + e);
      }
   }
}
