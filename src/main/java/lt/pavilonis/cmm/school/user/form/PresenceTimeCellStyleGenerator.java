package lt.pavilonis.cmm.school.user.form;

import com.vaadin.ui.StyleGenerator;
import lt.pavilonis.cmm.api.rest.presence.PresenceTime;

final class PresenceTimeCellStyleGenerator implements StyleGenerator<PresenceTime> {

   private static final String STYLE_RED = "red";
   private static final String STYLE_YELLOW = "yellow";
   private static final String STYLE_GREEN = "green";

   @Override
   public String apply(PresenceTime item) {

      if (item.getHourDifference() < 7) {
         return STYLE_RED;
      }

      return item.getHourDifference() < 8
            ? STYLE_YELLOW
            : STYLE_GREEN;
   }
}
