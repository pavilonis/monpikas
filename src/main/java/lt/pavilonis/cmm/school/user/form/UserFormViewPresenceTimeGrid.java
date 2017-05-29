package lt.pavilonis.cmm.school.user.form;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import lt.pavilonis.cmm.api.rest.presence.PresenceTime;
import lt.pavilonis.cmm.common.ListGrid;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class UserFormViewPresenceTimeGrid extends ListGrid<PresenceTime> {

   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

   UserFormViewPresenceTimeGrid(List<PresenceTime> items) {
      super(PresenceTime.class);

      setItems(items);

      setHeight(430, Unit.PIXELS);
      addStyleName("table-border-less");
   }

   @Override
   protected List<String> columnOrder() {
      return Arrays.asList("date", "startTime", "endTime", "hourDifference");
   }

   @Override
   protected Map<String, ValueProvider<PresenceTime, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "startTime", item -> TIME_FORMAT.format(item.getStartTime()),
            "hourDifference", PresenceTime::getHourDifference,
            "endTime", item -> TIME_FORMAT.format(item.getEndTime())
      );
   }

   @Override
   protected void customize() {
      getColumn("hourDifference").setStyleGenerator(new PresenceTimeCellStyleGenerator());
      getColumn("date").setWidth(130);
   }
}