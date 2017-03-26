package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.ui.renderers.NumberRenderer;
import lt.pavilonis.cmm.common.ListGrid;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;
import lt.pavilonis.cmm.repository.UserRestRepository;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

final class UserFormViewPresenceTimeTabGrid extends ListGrid<PresenceTimeRepresentation> {

   private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

   UserFormViewPresenceTimeTabGrid(List<PresenceTimeRepresentation> items) {
      super(PresenceTimeRepresentation.class);

      setItems(items);

      setHeight(430, Unit.PIXELS);
      addStyleName("table-border-less");


//      Column<PresenceTimeRepresentation, ?> startTime = getColumn("startTime");
      addColumn(item -> TIME_FORMAT.format(item.getStartTime()));
      addColumn(item -> TIME_FORMAT.format(item.getEndTime()))
            .setStyleGenerator(new PresenceTimeCellStyleGenerator());
      addColumn(PresenceTimeRepresentation::getHourDifference, new NumberRenderer() {

      });
//      setStyleGenerator(new StyleGenerator<PresenceTimeRepresentation>() {
//         @Override
//         public String apply(PresenceTimeRepresentation item) {
//            return null;
//         }
//      });
//      addColumn("startTime", );
//      setConverter("startTime", TIME_CONVERTER);
//      setConverter("endTime", TIME_CONVERTER);
//      setCellStyleGenerator(new PresenceTimeCellStyleGenerator());
   }

   @Override
   protected List<String> getProperties(Class<PresenceTimeRepresentation> type) {
      return Arrays.asList("date", "startTime", "endTime", "hourDifference");
   }
}
