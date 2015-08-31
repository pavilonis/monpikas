package lt.pavilonis.monpikas.server.reports;

import lt.pavilonis.monpikas.server.domain.MealEventLog;
import lt.pavilonis.monpikas.server.domain.MealType;
import lt.pavilonis.monpikas.server.domain.PupilType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.reverseOrder;
import static lt.pavilonis.monpikas.server.utils.Messages.label;
import static org.apache.poi.hssf.usermodel.HSSFPrintSetup.A4_PAPERSIZE;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;

public class Report {

   private final ReportHelper helper;
   private final HSSFSheet sheet;
   private final HSSFWorkbook workbook;
   private final String periodTitle;
   private final List<MealEventLog> events;
   private final Map<MealType, BigDecimal> sums = new HashMap<>();

   public Report(String periodTitle, List<MealEventLog> events) {
      this.workbook = new HSSFWorkbook();
      this.sheet = workbook.createSheet("Ataskaita");
      this.helper = new ReportHelper(sheet);
      this.periodTitle = periodTitle;
      this.events = events;
   }

   public HSSFWorkbook create(PupilType pupilType) {

      formatPage();
      setColumnWidths();

      helper.cell(0, 0, label("Report.Title")).bold().mergeTo(6).create();
      helper.cell(0, 1, periodTitle).bold().mergeTo(6).create();
      helper.cell(0, 3, label("PupilType." + pupilType)).mergeTo(6).create();

      for (MealType type : MealType.values()) {

         Map<Long, List<MealEventLog>> pupilMealsMap = events.stream()
               .filter(event -> event.getMealType() == type)
               .collect(Collectors.groupingBy(MealEventLog::getCardId));

         if (!pupilMealsMap.isEmpty()) {
            int rowNum = lastRow(2);
            helper.cell(0, rowNum++, label("MealType." + type)).mergeTo(6).bold().heigth(500).create();
            helper.cell(0, rowNum, "Eil.\nnr.").bold().heigth(900).create();
            helper.cell(1, rowNum, "Vardas Pavardė").bold().create();
            helper.cell(2, rowNum, "Kl.").bold().create();
            helper.cell(3, rowNum, "Maitinimosi dienos").bold().create();
            helper.cell(4, rowNum, "Viso\nmaitinta\ndienų").bold().create();
            helper.cell(5, rowNum, "Skirta\nlėšų\ndienai").bold().create();
            helper.cell(6, rowNum, "Viso\npanaudota\nlėšų").bold().create();

            sums.put(type, BigDecimal.ZERO);

            int index = 1;
            for (List<MealEventLog> events : pupilMealsMap.values()) {
               sums.put(type, sums.get(type).add(printCalculate(index++, events)));
            }
         }
      }


      helper.cell(3, lastRow(2), "Viso panaudota lėšų:").align(ALIGN_RIGHT).noBorder().create();

      sums.forEach((k, v) -> {
         helper.cell(4, lastRow() + 1, label("MealType." + k)).mergeTo(5).align(ALIGN_RIGHT).noBorder().create();
         helper.cell(6, lastRow(), v.toString()).noBorder().bold().create();
      });

      BigDecimal total = sums.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      helper.cell(5, lastRow(1), "VISO").noBorder().align(ALIGN_RIGHT).create();
      helper.cell(6, lastRow(), total).noBorder().bold().create();

      helper.cell(1, lastRow(2), "Socialinis pedagogas").noBorder().create();
      helper.cell(3, lastRow(), "Darius Jucys").align(ALIGN_RIGHT).noBorder().create();

      return workbook;
   }

   private BigDecimal printCalculate(int index, List<MealEventLog> events) {

      String mealDaysString = "";
      int mealDaysCount = 0;
      BigDecimal priceSum = BigDecimal.ZERO;

      Collections.sort(events, reverseOrder());

      for (MealEventLog event : events) {
         Calendar c = Calendar.getInstance();
         c.setTime(event.getDate());
         mealDaysString += c.get(Calendar.DAY_OF_MONTH) + " ";
         priceSum = priceSum.add(event.getPrice());
         mealDaysCount++;
      }

      int row = lastRow(1);
      helper.cell(0, row, index).create();
      helper.cell(1, row, events.get(0).getName()).align(ALIGN_LEFT).create();
      helper.cell(2, row, events.get(0).getGrade()).create();
      helper.cell(3, row, mealDaysString).align(ALIGN_LEFT).create();
      helper.cell(4, row, mealDaysCount).create();
      helper.cell(5, row, events.get(0).getPrice()).create();
      helper.cell(6, row, priceSum).create();

      return priceSum;
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

   private int lastRow(int plus) {
      return sheet.getLastRowNum() + plus;
   }

   private int lastRow() {
      return lastRow(0);
   }
}
