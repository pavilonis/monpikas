package lt.pavilonis.monpikas.server.reports;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.PupilType;
import lt.pavilonis.monpikas.server.repositories.MealEventLogRepository;
import lt.pavilonis.monpikas.server.repositories.PupilRepository;
import lt.pavilonis.monpikas.server.service.PupilService;
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

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportService {

   @Autowired
   private MealEventLogRepository mealEventLogRepository;

   private static final Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private static final Logger LOG = getLogger(PupilService.class);

   public ByteArrayOutputStream generate(Date from, Date to, PupilType pupilType) {

      List<MealEventLog> events = mealEventLogRepository.findByDateBetweenAndPupilType(from, to, pupilType);

      String reportPeriod = DATE_FORMAT.format(from) + "  -  " + DATE_FORMAT.format(to);
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
