package lt.pavilonis.monpikas.server.reports;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.util.OptionalInt;

import static java.util.OptionalInt.empty;
import static org.apache.poi.hssf.usermodel.HSSFCellStyle.BORDER_THIN;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;


public class ReportHelper {

   private HSSFSheet sheet;

   public ReportHelper(HSSFSheet sheet) {
      this.sheet = sheet;
   }

   public HSSFCellStyle style(HSSFWorkbook wb, int size, boolean bold, boolean border) {
      return style(wb, size, bold, border, ALIGN_CENTER);
   }

   public HSSFCellStyle style(HSSFWorkbook wb, int size, boolean bold, boolean border, short halign) {
      HSSFCellStyle style = wb.createCellStyle();
      HSSFFont font = wb.createFont();
      font.setFontHeightInPoints((short) size);
      style.setVerticalAlignment(VERTICAL_CENTER);
      style.setAlignment(halign);
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

   public void cell(int col, int row, Object value) {
      cell(col, row, empty(), value, false, true, ALIGN_CENTER);
   }

   public void cell(int col, int row, Object value, short halign, boolean border) {
      cell(col, row, empty(), value, false, border, halign);
   }

   public void cell(int col, int row, Object value, short halign) {
      cell(col, row, empty(), value, false, true, halign);
   }

   public void headerCell(int col, int row, Object value) {
      cell(col, row, OptionalInt.of(900), value, true, true, ALIGN_CENTER);
   }

   private void cell(int col, int row, OptionalInt rowHeight, Object value, boolean bold, boolean border, short halign) {
      HSSFRow newRow = sheet.getRow(row) == null
            ? sheet.createRow(row)
            : sheet.getRow(row);

      rowHeight.ifPresent(h -> newRow.setHeight((short) (h)));

      HSSFCell cell = newRow.createCell(col);
      cell.setCellStyle(style(sheet.getWorkbook(), 10, bold, border, halign));
      cell.setCellValue(value.toString());
//      if (value instanceof String) {
//         cell.setCellValue((String) value);
//      } else {
//         cell.setCellValue(value instanceof BigDecimal
//                     ? ((BigDecimal) value).doubleValue()
//                     : (Integer) value
//         );
//         cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//      }
   }

   public void title(int startCol, int endCol, int row, String text, int height) {
      sheet.addMergedRegion(new CellRangeAddress(row, row, startCol, endCol));
      HSSFCell cell = sheet.getRow(row) == null
            ? sheet.createRow(row).createCell(startCol)
            : sheet.getRow(row).createCell(startCol);

      cell.setCellValue(text);
      cell.setCellStyle(style(sheet.getWorkbook(), height, false, false));
   }

   public void sumCell(int startCol, int endCol, int row, String title, String text, boolean border) {
      cell(startCol, row, OptionalInt.of(450), title, true, border, ALIGN_RIGHT);
      cell(endCol, row, OptionalInt.of(450), text, true, border, ALIGN_CENTER);
   }
}
