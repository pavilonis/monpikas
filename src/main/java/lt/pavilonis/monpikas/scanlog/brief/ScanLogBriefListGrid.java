package lt.pavilonis.monpikas.scanlog.brief;

import com.vaadin.data.ValueProvider;
import lt.pavilonis.monpikas.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

final class ScanLogBriefListGrid extends ListGrid<ScanLogBrief> {

   private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

   public ScanLogBriefListGrid() {
      super(ScanLogBrief.class);
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("dateTime", "scanner", "name", "role", "group", "cardCode", "supervisor");
   }

   @Override
   protected Map<String, ValueProvider<ScanLogBrief, ?>> getCustomColumns() {
      return Map.of("dateTime", item -> DATE_TIME_FORMAT.format(item.getDateTime()));
   }

   @Override
   protected List<String> columnsToCollapse() {
      return List.of("cardCode", "supervisor");
   }
}
