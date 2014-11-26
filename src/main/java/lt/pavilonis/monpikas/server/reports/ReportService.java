package lt.pavilonis.monpikas.server.reports;

import com.google.common.collect.Multimaps;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.repositories.MealEventRepository;
import lt.pavilonis.monpikas.server.repositories.PupilInfoRepository;
import lt.pavilonis.monpikas.server.service.PupilService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ReportService {

   @Autowired
   private MealEventRepository mealRepo;

   @Autowired
   private PupilInfoRepository infoRepo;

   private final static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private static final String REPORT_TITLE = "Socialiai remtinų mokinių maitinimas Nacionalinėje M.K. Čiurlionio menų mokykloje";
   private static final Logger LOG = getLogger(PupilService.class);

   public ByteArrayOutputStream generate(Date from, Date to) {

      HSSFWorkbook wb = new HSSFWorkbook();

      String reportPeriod = DATE_FORMAT.format(from) + "  -  " + DATE_FORMAT.format(to);

      HSSFSheet sheet = wb.createSheet("Ataskaita");

      List<MealEvent> events = mealRepo.findByDateBetween(from, to);
      events.forEach(e -> e.setInfo(ofNullable(infoRepo.findByCardId(e.getCardId()))));


      List<MealEvent> breakfastEvents = events.stream()
            .filter(e -> e.getType() == BREAKFAST)
            .collect(Collectors.toList());

      List<MealEvent> dinnerEvents = events.stream()
            .filter(e -> e.getType() == DINNER)
            .collect(Collectors.toList());


      new Report().create(
            REPORT_TITLE,
            reportPeriod,
            Multimaps.index(breakfastEvents, MealEvent::getCardId),
            Multimaps.index(dinnerEvents, MealEvent::getCardId),
            sheet
      );

      FileOutputStream fileOut;
      ByteArrayOutputStream baos = null;
      try {
         baos = new ByteArrayOutputStream();
         //fileOut = new FileOutputStream("/home/art/Desktop/meal_report.xlsx");
         wb.write(baos);
         baos.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      LOG.info("Report was generated");
      return baos;
   }

}
