package lt.pavilonis.monpikas.server.reports;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.lang.String.valueOf;
import static java.math.BigDecimal.ZERO;
import static java.util.Collections.reverseOrder;
import static org.apache.poi.hssf.usermodel.HSSFPrintSetup.A4_PAPERSIZE;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;

public class Report {

   private static final String REPORT_TITLE = "Socialiai remtinų mokinių maitinimas Nacionalinėje M.K. Čiurlionio menų mokykloje";

   private final ReportHelper helper;
   private final HSSFSheet sheet;
   private final HSSFWorkbook workbook;
   private final String periodTitle;
   private final List<MealEventLog> events;
   private BigDecimal breakfastSum = ZERO;
   private BigDecimal dinnerSum = BigDecimal.ZERO;

   public Report(String periodTitle, List<MealEventLog> events) {
      this.workbook = new HSSFWorkbook();
      this.sheet = workbook.createSheet("Ataskaita");
      this.helper = new ReportHelper(sheet);
      this.periodTitle = periodTitle;
      this.events = events;
   }

   //public HSSFWorkbook create(Multimap<Long, MealEvent> breakfastEvents, Multimap<Long, MealEvent> dinnerEvents) {
   public HSSFWorkbook create() {

      formatPage();
      setColumnWidths();


      helper.title(0, 6, 0, REPORT_TITLE, 14);
      helper.title(0, 6, 1, periodTitle, 14);

      printEvents();

      int row = sheet.getLastRowNum() + 2;
      helper.cell(3, row, "Viso panaudota lėšų:", ALIGN_RIGHT, false);

      merge(row, 4, 5);
      helper.sumCell(4, 6, row++, "Pusryčiams", breakfastSum.toString(), false);
      merge(row, 4, 5);
      helper.sumCell(4, 6, row++, "Pietūms", dinnerSum.toString(), false);
      helper.sumCell(5, 6, row++, "VISO", dinnerSum.add(breakfastSum).toString(), false);

      helper.cell(1, ++row, "Socialinis pedagogas", ALIGN_CENTER, false);
      helper.cell(3, row, "Darius Jucys", ALIGN_RIGHT, false);

      return workbook;
   }

   private void printEvents() {

      events.forEach(event -> {

//         int lastRow = sheet.getLastRowNum();
//         lastRow += 2;
//         helper.title(0, 6, lastRow++, label("PortionType." + event.getType()), 12);
//
//         helper.headerCell(0, lastRow, "Eil.\nnr.");
//         helper.headerCell(1, lastRow, "Vardas Pavardė");
//         helper.headerCell(2, lastRow, "Kl.");
//         helper.headerCell(3, lastRow, "Maitinimosi dienos");
//         helper.headerCell(4, lastRow, "Viso\nmaitinta\ndienų");
//         helper.headerCell(5, lastRow, "Skirta\nlėšų\ndienai");
//         helper.headerCell(6, lastRow, "Viso\npanaudota\nlėšų");
//
//         int rowNumber = 1;
//         for (long cardId : events.keySet()) {
//            String mealDaysString = "";
//            int mealDaysCount = 0;
//            final BigDecimal priceSum = BigDecimal.ZERO;
//            List<MealEvent> cardEvents = new ArrayList<>(events.get(cardId));
//            Collections.sort(cardEvents, reverseOrder());
//            for (MealEvent event : cardEvents) {
//               Calendar c = Calendar.getInstance();
//               c.setTime(event.getDate());
//               mealDaysString += c.get(Calendar.DAY_OF_MONTH) + " ";
//               priceSum = priceSum.add(event.getPrice());
//               mealDaysCount++;
//            }
//            int row = lastRow + rowNumber;
//
//            MealEvent event = cardEvents.get(0);
//            String grade = "";
//            String portionPrice = "";
//            event.getPupil().ifPresent(info -> {
//
//               Portion portion;
//               if (type == BREAKFAST) {
//                  portion = info.getBreakfastPortion();
//                  breakfastSum = portion == null ? breakfastSum : breakfastSum.add(priceSum);
//               } else {
//                  portion = info.getDinnerPortion();
//                  dinnerSum = portion == null ? dinnerSum : dinnerSum.add(priceSum);
//               }
//               portionPrice = portion == null ? "" : valueOf(portion.getPrice());
//               grade = info.getGrade();
//            });
//
//            helper.cell(0, row, rowNumber);
//            helper.cell(1, row, cardEvents.get(0).getName(), ALIGN_LEFT);
//            helper.cell(2, row, grade);
//            helper.cell(3, row, mealDaysString, ALIGN_LEFT);
//            helper.cell(4, row, mealDaysCount);
//            helper.cell(5, row, portionPrice);
//            helper.cell(6, row, priceSum);
//
//            rowNumber++;
//         }
//         helper.sumCell(5, 6, sheet.getLastRowNum() + 1, "VISO", valueOf(type == BREAKFAST ? breakfastSum : dinnerSum), true);
//
      });
   }


   private void setColumnWidths() {
      sheet.setColumnWidth(0, 1500);   //#
      sheet.setColumnWidth(1, 7000);   //name
      sheet.setColumnWidth(2, 1500);   //grade
      sheet.setColumnWidth(3, 17000);  //days
      sheet.setColumnWidth(4, 2500);   //days sum
      sheet.setColumnWidth(5, 2500);   //day price
      sheet.setColumnWidth(6, 3300);   //money used
   }

   private void formatPage() {
      sheet.getPrintSetup().setLandscape(true);
      sheet.getPrintSetup().setPaperSize(A4_PAPERSIZE);
      sheet.setMargin(Sheet.TopMargin, .1);
      sheet.setMargin(Sheet.BottomMargin, .1);
      sheet.setMargin(Sheet.RightMargin, .5);
      sheet.setMargin(Sheet.LeftMargin, .5);
   }

   private void merge(int row, int startCol, int endCol) {
      sheet.addMergedRegion(new CellRangeAddress(row, row, startCol, endCol));
   }
}
