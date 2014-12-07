package lt.pavilonis.monpikas.server.reports;

import com.google.common.collect.Multimap;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;
import static org.apache.poi.hssf.usermodel.HSSFPrintSetup.A4_PAPERSIZE;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;

public class Report {

   private ReportHelper helper;
   private HSSFSheet sheet;
   private BigDecimal breakfastSum = ZERO;
   private BigDecimal dinnerSum = BigDecimal.ZERO;

   public void create(String title, String period, Multimap<Long, MealEvent> breakfastEvents,
                      Multimap<Long, MealEvent> dinnerEvents, HSSFSheet sh) {
      sheet = sh;
      helper = new ReportHelper(sheet);
      sheet.getPrintSetup().setLandscape(true);
      sheet.getPrintSetup().setPaperSize(A4_PAPERSIZE);
      sheet.setMargin(Sheet.TopMargin, .1);
      sheet.setMargin(Sheet.BottomMargin, .1);
      sheet.setMargin(Sheet.RightMargin, .5);
      sheet.setMargin(Sheet.LeftMargin, .5);

      sheet.setColumnWidth(0, 1500);   //#
      sheet.setColumnWidth(1, 7000);   //name
      sheet.setColumnWidth(2, 1500);   //grade
      sheet.setColumnWidth(3, 17000);  //days
      sheet.setColumnWidth(4, 2500);   //days sum
      sheet.setColumnWidth(5, 2500);   //day price
      sheet.setColumnWidth(6, 3300);   //money used

      helper.title(0, 6, 0, title, 14);
      helper.title(0, 6, 1, period, 14);

      printEventsFor(breakfastEvents, BREAKFAST);
      printEventsFor(dinnerEvents, DINNER);

      int row = sheet.getLastRowNum() + 2;
      helper.cell(3, row, "Viso panaudota lėšų:", ALIGN_RIGHT, false);


      sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 5));
      helper.sumCell(4, 6, row++, "Pusryčiams", breakfastSum.toString(), false);
      sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 5));
      helper.sumCell(4, 6, row++, "Pietūms", dinnerSum.toString(), false);
      helper.sumCell(5, 6, row++, "VISO", dinnerSum.add(breakfastSum).toString(), false);

      helper.cell(1, ++row, "Socialinis pedagogas", ALIGN_CENTER, false);
      helper.cell(3, row, "Darius Jucys", ALIGN_RIGHT, false);
   }

   private void printEventsFor(Multimap<Long, MealEvent> events, PortionType type) {
      int lastRow = sheet.getLastRowNum();
      lastRow += 2;
      helper.title(0, 6, lastRow++, type == BREAKFAST ? "Pusryčiai" : "Pietus", 12);

      helper.headerCell(0, lastRow, "Eil.\nnr.");
      helper.headerCell(1, lastRow, "Vardas Pavardė");
      helper.headerCell(2, lastRow, "Kl.");
      helper.headerCell(3, lastRow, "Maitinimosi dienos");
      helper.headerCell(4, lastRow, "Viso\nmaitinta\ndienų");
      helper.headerCell(5, lastRow, "Skirta\nlėšų\ndienai");
      helper.headerCell(6, lastRow, "Viso\npanaudota\nlėšų");

      int rownumber = 1;
      for (long cardId : events.keySet()) {
         String mealDaysString = "";
         int mealDaysCount = 0;
         BigDecimal priceSum = BigDecimal.ZERO;
         List<MealEvent> cardEvents = new ArrayList<>(events.get(cardId));
         Collections.sort(cardEvents);
         for (MealEvent event : cardEvents) {
            Calendar c = Calendar.getInstance();
            c.setTime(event.getDate());
            mealDaysString += c.get(Calendar.DAY_OF_MONTH) + " ";
            priceSum = priceSum.add(event.getPrice());
            mealDaysCount++;
         }
         int row = lastRow + rownumber;

         MealEvent event = cardEvents.get(0);
         String grade = "";
         String portionPrice = "";
         if (event.getInfo().isPresent()) {
            PupilInfo info = event.getInfo().get();
            Portion portion;
            if (type == BREAKFAST) {
               portion = info.getBreakfastPortion();
               breakfastSum = portion == null ? breakfastSum : breakfastSum.add(priceSum);
            } else {
               portion = info.getDinnerPortion();
               dinnerSum = portion == null ? dinnerSum : dinnerSum.add(priceSum);
            }
            portionPrice = portion == null ? "" : String.valueOf(portion.getPrice());
            grade = info.getGrade();
         }

         helper.cell(0, row, rownumber);
         helper.cell(1, row, cardEvents.get(0).getName(), ALIGN_LEFT);
         helper.cell(2, row, grade);
         helper.cell(3, row, mealDaysString, ALIGN_LEFT);
         helper.cell(4, row, mealDaysCount);
         helper.cell(5, row, portionPrice);
         helper.cell(6, row, priceSum);

         rownumber++;
      }
      helper.sumCell(5, 6, sheet.getLastRowNum() + 1, "VISO", String.valueOf(type == BREAKFAST ? breakfastSum : dinnerSum), true);
   }
}
