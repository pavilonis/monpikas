package lt.pavilonis.cmm.canteen.reports;

import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.repositories.MealEventLogRepository;
import lt.pavilonis.cmm.canteen.service.UserMealService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportService {

   @Autowired
   private MealEventLogRepository mealEventLogRepository;

   private static final Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private static final Logger LOG = getLogger(UserMealService.class);

   public ByteArrayOutputStream generate(Date periodStart, Date periodEnd, PupilType pupilType) {

      List<MealEventLog> events = mealEventLogRepository.load(pupilType, periodStart, periodEnd);

      String reportPeriod = DATE_FORMAT.format(periodStart) + "  -  " + DATE_FORMAT.format(periodEnd);
      HSSFWorkbook wb = new Report(reportPeriod, events).create(pupilType);

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
