package lt.pavilonis.cmm.canteen.report;

import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import lt.pavilonis.cmm.canteen.repository.EatingEventRepository;
import lt.pavilonis.cmm.canteen.service.UserEatingService;
import lt.pavilonis.cmm.canteen.ui.event.EatingEventFilter;
import lt.pavilonis.cmm.common.service.MessageSourceAdapter;
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

   private static final Logger LOG = getLogger(UserEatingService.class);

   @Autowired
   private EatingEventRepository eatingEventRepo;

   public ByteArrayOutputStream generate(LocalDate periodStart, LocalDate periodEnd, PupilType pupilType) {

      EatingEventFilter filter = new EatingEventFilter(null, periodStart, periodEnd, pupilType);
      List<EatingEvent> events = eatingEventRepo.load(filter);

      String reportPeriod = DateTimeFormatter.ISO_DATE.format(periodStart) +
            "  -  " + DateTimeFormatter.ISO_DATE.format(periodEnd);

      HSSFWorkbook wb = new Report(reportPeriod, events)
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
