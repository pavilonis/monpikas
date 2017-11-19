package lt.pavilonis.cmm.school.scanlog;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.api.rest.scanlog.ScanLogBrief;
import lt.pavilonis.cmm.api.rest.scanner.Scanner;
import lt.pavilonis.cmm.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class ScanLogBriefListGrid extends ListGrid<ScanLogBrief> {

   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

   public ScanLogBriefListGrid() {
      super(ScanLogBrief.class);
   }

   @Override
   protected List<String> columnOrder() {
      return Arrays.asList("dateTime", "scanner", "location", "name", "role", "group", "cardCode");
   }

   @Override
   protected Map<String, ValueProvider<ScanLogBrief, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "dateTime", item -> DATE_TIME_FORMAT.format(item.getDateTime()),
            "scanner", item -> messages.get(Scanner.class, item.getScanner())
      );
   }

   @Override
   protected List<String> columnsToCollapse() {
      return Collections.singletonList("cardCode");
   }
}
