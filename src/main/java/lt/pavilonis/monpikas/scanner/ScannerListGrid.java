package lt.pavilonis.monpikas.scanner;

import lt.pavilonis.monpikas.common.ListGrid;

import java.util.List;

final class ScannerListGrid extends ListGrid<Scanner> {

   public ScannerListGrid() {
      super(Scanner.class);
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("id", "name");
   }
}
