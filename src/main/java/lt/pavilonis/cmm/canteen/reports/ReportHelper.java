package lt.pavilonis.monpikas.server.reports;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Optional;
import java.util.OptionalInt;

import static org.apache.poi.hssf.usermodel.HSSFCellStyle.BORDER_THIN;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;

public class ReportHelper {

   private final HSSFSheet sheet;

   public ReportHelper(HSSFSheet sheet) {
      this.sheet = sheet;
   }

   public CellBuilder cell(int col, int row, Object value) {
      return new CellBuilder(col, row, value);
   }

   class CellBuilder {
      private int colNum;
      private int rowNum;
      private String value;
      private short hAlign = ALIGN_CENTER;
      private boolean bold;
      private boolean border = true;
      private Optional<Short> height = Optional.empty();
      private OptionalInt endColNum = OptionalInt.empty();

      public CellBuilder(int colNum, int rowNum, Object value) {
         this.colNum = colNum;
         this.rowNum = rowNum;
         this.value = String.valueOf(value);
      }

      public CellBuilder align(short hAlign) {
         this.hAlign = hAlign;
         return this;
      }

      public CellBuilder bold() {
         this.bold = true;
         return this;
      }

      public CellBuilder noBorder() {
         this.border = false;
         return this;
      }

      public CellBuilder heigth(int height) {
         this.height = Optional.of((short) height);
         return this;
      }

      public CellBuilder mergeTo(int endColNum) {
         this.endColNum = OptionalInt.of(endColNum);
         return this;
      }

      public void create() {
         endColNum.ifPresent(endCol -> sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, colNum, endCol)));

         HSSFRow row = sheet.getRow(rowNum) == null
               ? sheet.createRow(rowNum)
               : sheet.getRow(rowNum);

         height.ifPresent(row::setHeight);
         HSSFCellStyle style = style(sheet.getWorkbook(), 10, bold, border, hAlign);
         row.setRowStyle(style);

         HSSFCell cell = row.createCell(colNum);
         cell.setCellStyle(style);
         cell.setCellValue(value);
      }


      public HSSFCellStyle style(HSSFWorkbook wb, int fontSize, boolean bold, boolean border, short hAlign) {
         HSSFCellStyle style = wb.createCellStyle();
         HSSFFont font = wb.createFont();
         font.setFontHeightInPoints((short) fontSize);
         style.setVerticalAlignment(VERTICAL_CENTER);
         style.setAlignment(hAlign);
         if (border) {
            style.setBorderBottom(BORDER_THIN);
            style.setBorderRight(BORDER_THIN);
            style.setBorderLeft(BORDER_THIN);
            style.setBorderTop(BORDER_THIN);
         }
         if (bold) {
            font.setBoldweight((short) 12700);
         }
         style.setWrapText(true);
         style.setFont(font);
         return style;
      }
   }
}
