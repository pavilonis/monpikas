package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.ui.StyleGenerator;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;

final class PresenceTimeCellStyleGenerator implements StyleGenerator<PresenceTimeRepresentation> {

   private static final String STYLE_RED = "red";
   private static final String STYLE_YELLOW = "yellow";
   private static final String STYLE_GREEN = "green";

   @Override
   public String apply(PresenceTimeRepresentation item) {

      if (item.getHourDifference() < 7) {
         return STYLE_RED;
      }

      return item.getHourDifference() < 8
            ? STYLE_YELLOW
            : STYLE_GREEN;
   }
}
