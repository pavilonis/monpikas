package lt.pavilonis.cmm.canteen.report;

import lt.pavilonis.cmm.App;
import lt.pavilonis.cmm.canteen.domain.EatingEvent;
import lt.pavilonis.cmm.canteen.domain.EatingType;
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

public final class Report {

   private final ReportHelper helper;
   private final HSSFSheet sheet;
   private final HSSFWorkbook workbook;
   private final String periodTitle;
   private final List<EatingEvent> events;
   private final Map<EatingType, BigDecimal> sums = new HashMap<>();

   public Report(String periodTitle, List<EatingEvent> events) {
      this.workbook = new HSSFWorkbook();
      this.sheet = workbook.createSheet("Ataskaita");
      this.helper = new ReportHelper(sheet);
      this.periodTitle = periodTitle;
      this.events = events;
   }

   public HSSFWorkbook create(PupilType pupilType) {

      formatPage();
      setColumnWidths();

      helper.cell(0, 0, App.translate(this, "title")).bold().mergeTo(6).create();
      helper.cell(0, 1, periodTitle).bold().mergeTo(6).create();
      helper.cell(0, 3, App.translate(PupilType.class, pupilType.name())).mergeTo(6).create();

      for (EatingType eatingType : EatingType.values()) {

         Map<String, List<EatingEvent>> pupilEatingsMap = events.stream()
               .filter(event -> event.getEatingType() == eatingType)
               .collect(Collectors.groupingBy(EatingEvent::getCardCode));

         if (!pupilEatingsMap.isEmpty()) {
            int rowNum = lastRow(2);
            helper.cell(0, rowNum++, App.translate(EatingType.class, eatingType.name()))
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

            sums.put(eatingType, BigDecimal.ZERO);

            int index = 1;
            for (List<EatingEvent> events : pupilEatingsMap.values()) {
               sums.put(eatingType, sums.get(eatingType).add(printCalculate(index++, events)));
            }
         }
      }

      helper.cell(3, lastRow(2), "Viso panaudota lėšų:")
            .align(HorizontalAlignment.RIGHT)
            .noBorder()
            .create();

      sums.forEach((eatingType, amount) -> {
         helper.cell(4, lastRow() + 1, App.translate(EatingType.class, eatingType.name()))
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

   private BigDecimal printCalculate(int index, List<EatingEvent> events) {

      String eatingDaysString = "";
      int eatingDaysCount = 0;
      BigDecimal priceSum = BigDecimal.ZERO;

      Collections.sort(events, reverseOrder());

      for (EatingEvent event : events) {
         Calendar c = Calendar.getInstance();
         c.setTime(event.getDate());
         eatingDaysString += c.get(Calendar.DAY_OF_MONTH) + " ";
         priceSum = priceSum.add(event.getPrice());
         eatingDaysCount++;
      }

      int row = lastRow(1);
      helper.cell(0, row, index).create();
      helper.cell(1, row, events.get(0).getName()).align(HorizontalAlignment.LEFT).create();

      String grade = StringUtils.substring(events.get(0).getGrade(), 0, 5);
      helper.cell(2, row, grade).create();

      helper.cell(3, row, eatingDaysString).align(HorizontalAlignment.LEFT).create();
      helper.cell(4, row, eatingDaysCount).create();
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
