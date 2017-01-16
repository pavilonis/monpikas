package lt.pavilonis.cmm.canteen.report;

import lt.pavilonis.cmm.MessageSourceAdapter;
import lt.pavilonis.cmm.canteen.domain.MealEventLog;
import lt.pavilonis.cmm.canteen.domain.MealType;
import lt.pavilonis.cmm.canteen.domain.PupilType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static org.apache.poi.hssf.usermodel.HSSFPrintSetup.A4_PAPERSIZE;

public class Report {

   private final MessageSourceAdapter messages;
   private final ReportHelper helper;
   private final HSSFSheet sheet;
   private final HSSFWorkbook workbook;
   private final String periodTitle;
   private final List<MealEventLog> events;
   private final Map<MealType, BigDecimal> sums = new HashMap<>();

   public Report(MessageSourceAdapter messages, String periodTitle, List<MealEventLog> events) {
      this.messages = messages;
      this.workbook = new HSSFWorkbook();
      this.sheet = workbook.createSheet("Ataskaita");
      this.helper = new ReportHelper(sheet);
      this.periodTitle = periodTitle;
      this.events = events;
   }

   public HSSFWorkbook create(PupilType pupilType) {

      formatPage();
      setColumnWidths();

      helper.cell(0, 0, messages.get(this, "title")).bold().mergeTo(6).create();
      helper.cell(0, 1, periodTitle).bold().mergeTo(6).create();
      helper.cell(0, 3, messages.get(PupilType.class, pupilType.name())).mergeTo(6).create();

      for (MealType mealType : MealType.values()) {

         Map<String, List<MealEventLog>> pupilMealsMap = events.stream()
               .filter(event -> event.getMealType() == mealType)
               .collect(Collectors.groupingBy(MealEventLog::getCardCode));

         if (!pupilMealsMap.isEmpty()) {
            int rowNum = lastRow(2);
            helper.cell(0, rowNum++, messages.get(MealType.class, mealType.name()))
                  .mergeTo(6)
                  .bold()
                  .heigth(500)
                  .create();

            helper.cell(0, rowNum, "Eil.\nnr.").bold().heigth(900).create();
            helper.cell(1, rowNum, "Vardas Pavardė").bold().create();
            helper.cell(2, rowNum, "Kl.").bold().create();
            helper.cell(3, rowNum, "Maitinimosi dienos").bold().create();
            helper.cell(4, rowNum, "Viso\nmaitinta\ndienų").bold().create();
            helper.cell(5, rowNum, "Skirta\nlėšų\ndienai").bold().create();
            helper.cell(6, rowNum, "Viso\npanaudota\nlėšų").bold().create();

            sums.put(mealType, BigDecimal.ZERO);

            int index = 1;
            for (List<MealEventLog> events : pupilMealsMap.values()) {
               sums.put(mealType, sums.get(mealType).add(printCalculate(index++, events)));
            }
         }
      }

      helper.cell(3, lastRow(2), "Viso panaudota lėšų:")
            .align(HorizontalAlignment.RIGHT)
            .noBorder()
            .create();

      sums.forEach((mealType, amount) -> {
         helper.cell(4, lastRow() + 1, messages.get(MealType.class, mealType.name()))
               .mergeTo(5)
               .align(HorizontalAlignment.RIGHT)
               .noBorder()
               .create();

         helper.cell(6, lastRow(), amount.toString())
               .noBorder()
               .bold()
               .create();
      });

      BigDecimal total = sums.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);

      helper.cell(5, lastRow(1), "VISO")
            .noBorder()
            .align(HorizontalAlignment.RIGHT)
            .create();

      helper.cell(6, lastRow(), total).noBorder().bold().create();

      helper.cell(1, lastRow(2), "Socialinis pedagogas")
            .noBorder()
            .create();

      helper.cell(3, lastRow(), "Darius Jucys")
            .align(HorizontalAlignment.RIGHT)
            .noBorder()
            .create();

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
      helper.cell(1, row, events.get(0).getName()).align(HorizontalAlignment.LEFT).create();

      String grade = StringUtils.substring(events.get(0).getGrade(), 0, 5);
      helper.cell(2, row, grade).create();

      helper.cell(3, row, mealDaysString).align(HorizontalAlignment.LEFT).create();
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

   private int lastRow(int plus) {
      return sheet.getLastRowNum() + plus;
   }

   private int lastRow() {
      return lastRow(0);
   }
}
