package lt.pavilonis.cmm.ui.user.form;

import com.google.common.collect.ImmutableMap;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.renderers.NumberRenderer;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final class UserFormViewPresenceTimeGrid extends ListGrid<PresenceTimeRepresentation> {

   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

   UserFormViewPresenceTimeGrid(List<PresenceTimeRepresentation> items) {
      super(PresenceTimeRepresentation.class);

      setItems(items);

      setHeight(430, Unit.PIXELS);
      addStyleName("table-border-less");
   }

   @Override
   protected List<String> getProperties(Class<PresenceTimeRepresentation> type) {
      return Arrays.asList("date", "startTime", "endTime", "hourDifference");
   }

   @Override
   protected Map<String, ValueProvider<PresenceTimeRepresentation, ?>> getCustomColumns() {
      return ImmutableMap.of(
            "startTime", item -> TIME_FORMAT.format(item.getStartTime()),
            "hourDifference", PresenceTimeRepresentation::getHourDifference,
            "endTime", item -> TIME_FORMAT.format(item.getEndTime())
      );
   }

   @Override
   protected void customize() {
      getColumn("hourDifference").setStyleGenerator(new PresenceTimeCellStyleGenerator());
      getColumn("date").setWidth(130);
   }
}
