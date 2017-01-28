package lt.pavilonis.cmm.ui.user.form;

import com.vaadin.ui.Table;
import lt.pavilonis.cmm.domain.PresenceTimeRepresentation;

public class PresenceTimeCellStyleGenerator implements Table.CellStyleGenerator {

   private static final String STYLE_RED = "red";
   private static final String STYLE_YELLOW = "yellow";
   private static final String STYLE_GREEN = "green";
   private static final String PROPERTY_HOUR_DIFFERENCE = "hourDifference";

   @Override
   public String getStyle(Table source, Object itemId, Object propertyId) {
      if (!PROPERTY_HOUR_DIFFERENCE.equals(propertyId)) {
         return null;
      }

      PresenceTimeRepresentation time = (PresenceTimeRepresentation) itemId;

      if (time.getHourDifference() < 7) {
         return STYLE_RED;
      }

      return time.getHourDifference() < 8
            ? STYLE_YELLOW
            : STYLE_GREEN;
   }
}
