package lt.pavilonis.monpikas.server.reports;

import com.google.common.collect.Multimap;
import lt.pavilonis.monpikas.server.domain.MealEvent;
import lt.pavilonis.monpikas.server.domain.Portion;
import lt.pavilonis.monpikas.server.domain.PupilInfo;
import lt.pavilonis.monpikas.server.domain.enumeration.PortionType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.BREAKFAST;
import static lt.pavilonis.monpikas.server.domain.enumeration.PortionType.DINNER;
import static org.apache.poi.hssf.usermodel.HSSFPrintSetup.A4_PAPERSIZE;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;

public class Report {

   private ReportHelper helper;
   private HSSFSheet sheet;
   private double breakfastSum = 0;
   private double dinnerSum = 0;
   DecimalFormat df = new DecimalFormat("0.00");

   public void create(String title, String period, Multimap<Long, MealEvent> breakfastEvents,
                      Multimap<Long, MealEvent> dinnerEvents, HSSFSheet sh) {
      sheet = sh;
      helper = new ReportHelper(sheet);
      sheet.getPrintSetup().setLandscape(true);
      sheet.getPrintSetup().setPaperSize(A4_PAPERSIZE);
      sheet.setMargin(Sheet.TopMargin, 0);
      sheet.setMargin(Sheet.BottomMargin, 0);
      sheet.setMargin(Sheet.RightMargin, 0);
      sheet.setMargin(Sheet.LeftMargin, 0);

      sheet.setColumnWidth(0, 1000);
      sheet.setColumnWidth(1, 6700);
      sheet.setColumnWidth(2, 1000);
      sheet.setColumnWidth(3, 17000);
      sheet.setColumnWidth(4, 2200);
      sheet.setColumnWidth(5, 2200);
      sheet.setColumnWidth(6, 2600);

      helper.title(0, 6, 0, title, 14);
      helper.title(0, 6, 1, period, 14);

      printEventsFor(breakfastEvents, BREAKFAST);
      printEventsFor(dinnerEvents, DINNER);

      int row = sheet.getLastRowNum() + 2;
      helper.cell(3, row, "Viso panaudota lėšų:", ALIGN_RIGHT, false);


      sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 5));
      helper.sumCell(4, 6, row++, "Pusryčiams", df.format(breakfastSum), false);
      sheet.addMergedRegion(new CellRangeAddress(row, row, 4, 5));
      helper.sumCell(4, 6, row++, "Pietūms", df.format(dinnerSum), false);
      helper.sumCell(5, 6, row++, "VISO", df.format(dinnerSum + breakfastSum), false);

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
         double priceSum = 0.0;
         List<MealEvent> cardEvents = new ArrayList<>(events.get(cardId));
         Collections.sort(cardEvents);
         for (MealEvent event : cardEvents) {
            Calendar c = Calendar.getInstance();
            c.setTime(event.getDate());
            mealDaysString += c.get(Calendar.DAY_OF_MONTH) + " ";
            priceSum += event.getPrice();
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
               breakfastSum = portion == null ? breakfastSum : breakfastSum + priceSum;
            } else {
               portion = info.getDinnerPortion();
               dinnerSum = portion == null ? dinnerSum : dinnerSum + priceSum;
            }
            portionPrice = portion == null ? "" : df.format(portion.getPrice());
            grade = info.getGrade();
         }

         helper.cell(0, row, rownumber);
         helper.cell(1, row, cardEvents.get(0).getName(), ALIGN_LEFT);
         helper.cell(2, row, grade);
         helper.cell(3, row, mealDaysString, ALIGN_LEFT);
         helper.cell(4, row, mealDaysCount);
         helper.cell(5, row, portionPrice);
         helper.cell(6, row, df.format(priceSum));

         rownumber++;
      }
      helper.sumCell(5, 6, sheet.getLastRowNum() + 1, "VISO", String.valueOf(type == BREAKFAST ? breakfastSum : dinnerSum), true);
   }
}
