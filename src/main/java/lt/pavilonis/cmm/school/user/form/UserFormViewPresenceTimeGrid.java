package lt.pavilonis.cmm.school.user.form;

import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.school.user.PresenceTime;
import lt.pavilonis.cmm.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

final class UserFormViewPresenceTimeGrid extends ListGrid<PresenceTime> {

   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

   UserFormViewPresenceTimeGrid() {
      super(PresenceTime.class);
      setHeight(362, Unit.PIXELS);
      addStyleName("table-border-less");
   }

   @Override
   protected List<String> columnOrder() {
      return List.of("date", "startTime", "endTime", "hourDifference");
   }

   @Override
   protected Map<String, ValueProvider<PresenceTime, ?>> getCustomColumns() {
      return Map.of(
            "startTime", item -> TIME_FORMAT.format(item.getStartTime()),
            "hourDifference", PresenceTime::getHourDifference,
            "endTime", item -> TIME_FORMAT.format(item.getEndTime())
      );
   }

   @Override
   protected void customize() {
      getColumn("hourDifference").setStyleGenerator(new PresenceTimeCellStyleGenerator());
      getColumn("date").setWidth(130);
      getColumn("startTime").setWidth(100);
      getColumn("endTime").setWidth(100);
      getColumn("hourDifference").setWidth(600);
   }
}
